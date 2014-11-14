#include <jni.h>
#include <Symbol.h>
#include "yoyon_types.h"
#include <android/log.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <fcntl.h>
#include <stdio.h>
#include <sys/stat.h>  //stat函数可以返回一个结构，里面包括文件的全部属性。
#include <string.h>
#include <assert.h>
#include <stdarg.h>	   //主要目的为让函数能够接收可变参数。

#ifndef NULL
#define NULL ((void *)0)
#endif

#ifndef TRUE
#define TRUE 1
#endif

#ifndef FALSE
#define FALSE 0
#endif

#define B_LOG_ON TRUE		//控件需不需要打印
#define B_NEED_COMMON_DELAY FALSE

#if B_LOG_ON
#define LOG_D(...) __android_log_print(ANDROID_LOG_DEBUG,"JNI",__VA_ARGS__)
#else
#define LOG_D(...)
#endif

#if B_NEED_COMMON_DELAY
#define DelayMs(ms) do{\
		usleep(ms);\
}while(0)
#define CommonDelayMs 100
#define CommonDelay() do{\
		DelayMs(CommonDelayMs);\
}while(0)
#else
#define CommonDelay()
#endif

//用来对指纹库api互斥调用的互斥锁
static pthread_mutex_t hMutex = PTHREAD_MUTEX_INITIALIZER;
#define THREAD_LOCK() do{\
	pthread_mutex_lock(&hMutex);\
}while(0)
#define THREAD_UNLOCK() do{\
	pthread_mutex_unlock(&hMutex);\
}while(0)


//删除单个指纹
jboolean JNICALL deleteFP(JNIEnv * env, jobject object, jlong id) {
	int w_nRet = -1;
	THREAD_LOCK()
	;
	w_nRet = CommandProcess(CMD_CLEAR_TEMPLATE_CODE, id, 0, 0);
	THREAD_UNLOCK()
	;
	if (w_nRet != ERR_SUCCESS) {
		LOG_D("fp:Delete  template failed!!");
		return false;
	}
	LOG_D("fp:Delete  template w_nRet = %d", w_nRet);

	return true;
}
//删除所有指纹
jboolean JNICALL deleteAllFP(JNIEnv * env, jobject object) {
	int w_nRet = -1;
	THREAD_LOCK()
	;
	w_nRet = CommandProcess(CMD_CLEAR_ALLTEMPLATE_CODE, 0, 0, 0);
	THREAD_UNLOCK()
	;
	if (w_nRet != ERR_SUCCESS) {
		LOG_D("Delete all template failed!!");
		return false;
	}
	LOG_D("Delete all template w_nRet = %d", w_nRet);
	return true;

}

//读取模板
jboolean JNICALL readTemplate(JNIEnv * env, jobject object, jlong id,
		jbyteArray templateBytes) {

	jbyte* template_buff = NULL;
	//读取模板数据，并存到template_buff指向的缓存
	THREAD_LOCK();
	int w_nRet = CommandProcess(CMD_READ_TEMPLATE_CODE, id,
			(UINT32) &template_buff, 0);
	THREAD_UNLOCK();
	if (w_nRet == ERR_SUCCESS && template_buff) {
		//定义一个指针指向存储这个byteArray 的内存地址
		jbyte* p_templateBytes = env->GetByteArrayElements(templateBytes, NULL);
		memcpy(p_templateBytes,template_buff, GD_USER_RECORD_SIZE); //把本地缓存中的数据copy到byteArray
		free(template_buff); //使用完后，释放内存
		LOG_D(" fp: read template Success");
		env->ReleaseByteArrayElements(templateBytes, p_templateBytes, 0); //释放java中byteArray引用
		return true;
	} else {
		LOG_D("fp: read template fail");
		return false;
	}

}
//写入模板
jboolean JNICALL writeTemplate(JNIEnv * env, jobject object, jint id,
		jbyteArray templateBytes) {
	LOG_D("begin write template id = %d", id);
	jbyte* p_templateBytes = env->GetByteArrayElements(templateBytes, NULL);
	THREAD_LOCK()
	;
	int w_nRet = CommandProcess(CMD_WRITE_TEMPLATE_CODE, id,
			(UINT32) p_templateBytes, 0);
	THREAD_UNLOCK()
	;
	LOG_D(" write template w_nRet = 0x%02x ", w_nRet);
	env->ReleaseByteArrayElements(templateBytes, p_templateBytes, 0);
	if (w_nRet != ERR_SUCCESS) {
		LOG_D("writeTemplate failed!");
		return false;
	}
	return true;

}

//获取显示的图像
jboolean JNICALL getFingerImage(JNIEnv * env, jobject object,
		jbyteArray imageByteData, jlongArray imageSize) {
	int w_nRet = -1;

	/*GetByteArrayElements-->把java的Byte数组转换到C本地数组，有两种方式，一种是拷贝一份，另一种是传回一个指向java数组的指针，
	 * 显然，这里是第二种方式*/
	jbyte* image_bytes = env->GetByteArrayElements(imageByteData, NULL);
	int image_w = 0, image_h = 0;
	//读取图像数据，并保存到image_bytes指向的内存中
	THREAD_LOCK();
	w_nRet = CommandProcess(CMD_GET_FRAME_CODE, (UINT32) image_bytes,
			(UINT32) &image_h, (UINT32) &image_w);
	THREAD_UNLOCK();
	env->ReleaseByteArrayElements(imageByteData, image_bytes, 0);
	if (w_nRet != ERR_SUCCESS) {
		LOG_D("fp:get finger image failed!");
		return false;
	}
	//把图像的大小信息写到imageSize的内存中
	jlong* p_image_size = env->GetLongArrayElements(imageSize, NULL);
	p_image_size[0] = image_w;
	p_image_size[1] = image_h;
	env->ReleaseLongArrayElements(imageSize, p_image_size, 0);

	return true;
}

jboolean JNICALL identifyFP(JNIEnv * env, jobject object, jlongArray id) {
	int w_nRet = -1;
	UINT32 idBuff = 0;
	UINT32 learnBuff = 0;
	LOG_D("begin identify");
	THREAD_LOCK()
	;
	w_nRet = CommandProcess(CMD_IDENTIFY_CODE, (UINT32) &idBuff,
			(UINT32) &learnBuff, 0); //验证指纹
	THREAD_UNLOCK()
	;
	if (w_nRet == ERR_SUCCESS) {
		jlong *pid = (env)->GetLongArrayElements(id, NULL);
		pid[0] = idBuff;				//把识别到的指纹id传递回去
		(env)->ReleaseLongArrayElements(id, pid, 0);
		return true;
	}
	return false;

}

//获取识别图像
jboolean JNICALL getIdentifyImage(JNIEnv * env, jobject object) {
	int w_nRet = -1;
	THREAD_LOCK()
	;
	w_nRet = CommandProcess(CMD_GET_IMAGE_CODE, CMD_IDENTIFY_CODE, 0, 0);
	THREAD_UNLOCK()
	;
	if (w_nRet != ERR_SUCCESS) {
//					LOG_D("getIdentifyImage failed!");
		return false;
	}
	return true;
}

//合成指纹模板
jint JNICALL enrollCompose(JNIEnv * env, jobject object, jlong enrollId) {
	int w_nRet = -1;
	int Dup_No = 3; //合成个数为3
	THREAD_LOCK()
	;
	w_nRet = CommandProcess(CMD_ENROLL_CODE, 4, enrollId, (UINT32) &Dup_No); //合成模板
	THREAD_UNLOCK()
	;
	LOG_D("the compose result is 0x%x", w_nRet);
	if (w_nRet != ERR_SUCCESS) {
		LOG_D("enroll compose failed!");

	}
	return w_nRet;
}

//检测手指是否离开
jboolean JNICALL loopDetectFinger(JNIEnv * env, jobject object) {
	int w_nRet = GD_DETECT_FINGER;
	THREAD_LOCK()
	;
	while (w_nRet == GD_DETECT_FINGER) {
		w_nRet = CommandProcess(CMD_FINGER_DETECT_CODE, 0, 0, 0);  //等待手指松开
	}
	THREAD_UNLOCK()
	;
	LOG_D("finger have leaved!");
	return true;
}

//检测是否有手指按下
jboolean JNICALL detectFinger(JNIEnv * env, jobject object) {
	int w_nRet = -1;
	THREAD_LOCK()
	;
	w_nRet = CommandProcess(CMD_FINGER_DETECT_CODE, 0, 0, 0);  //等待手指状态
	THREAD_UNLOCK()
	;
	if (w_nRet == GD_DETECT_FINGER) {
		return true;
	} else {
		return false;
	}
}

//第几次注册
jboolean JNICALL enrollTimes(JNIEnv * env, jobject object, jlong enrollId,
		jint time) {
	int w_nRet = -1;
	THREAD_LOCK()
	;
	w_nRet = CommandProcess(CMD_ENROLL_CODE, time, enrollId, 0);
	THREAD_UNLOCK()
	;
	if (w_nRet != ERR_SUCCESS) {
		LOG_D("enroll failed!");
		return false;
	}
	return true;
}

//从Sensor采集指纹图像并从该图像生成template
jboolean JNICALL getEnrollImage(JNIEnv * env, jobject object) {
	int w_nRet = -1;
	THREAD_LOCK()
	;
	w_nRet = CommandProcess(CMD_GET_IMAGE_CODE, CMD_ENROLL_CODE, 0, 0); //获取图像
	THREAD_UNLOCK()
	;
	if (w_nRet != ERR_SUCCESS) {
//		LOG_D("get enroll image failed!");
		return false;
	}
	return true;
}

//发出注册命令
jboolean JNICALL enrollStart(JNIEnv * env, jobject object, jlong enrollId) {
	int w_nRet = -1;
	THREAD_LOCK();
	w_nRet = CommandProcess(CMD_ENROLL_CODE, 0, enrollId, 0);
	THREAD_UNLOCK();
	if (ERR_SUCCESS != w_nRet) {
		LOG_D("enrollStart failed!");
		return false;
	}
	return true;
}

jboolean JNICALL getAvaliableId(JNIEnv * env, jobject object, jlongArray id) {
	int w_nRet = -1;
	UINT16 w_nFpID = 0;
	THREAD_LOCK();
	w_nRet = CommandProcess(CMD_GET_EMPTY_ID_CODE, (UINT32) &w_nFpID, 0, 0); //获取可用ID
	THREAD_UNLOCK();
	if (w_nRet == ERR_SUCCESS) {
		jlong* pid = env->GetLongArrayElements(id, NULL);
		pid[0] = w_nFpID;						//把获得的指纹id传递回java
		env->ReleaseLongArrayElements(id, pid, 0);
		return true;
	}
	LOG_D("get empty id Failed");
	return false;
}

//启动时必须调用此指令。并且调用之前,务必初始化
jboolean JNICALL initFP(JNIEnv *env, jobject object) {
	int w_nRet = -1;
	LOG_D("begin init");CommonDelay();
	THREAD_LOCK()
	;
	w_nRet = CommandProcess(CMD_INIT_FPLIB_CODE, 0, 0, 0);
	THREAD_UNLOCK()
	;
	if (ERR_SUCCESS != w_nRet) {
		LOG_D("init failed!,the w_nRet=0x%x",w_nRet);
		return false;
	}
	LOG_D("init success!");
	return true;
}

//关闭指纹传感器
jboolean JNICALL stopFP(JNIEnv *env, jobject object) {
	int w_nRet = -1;
	THREAD_LOCK();
	w_nRet = CommandProcess(CMD_STOP_FPLIB_CODE, 0, 0, 0);
	THREAD_UNLOCK();
	if (ERR_SUCCESS != w_nRet) {
		LOG_D("stop failed!");
		return false;
	}
	LOG_D("stop success!");
	return true;

}

//设置指纹是否可重复
jboolean JNICALL  setDuplicateCheck(JNIEnv *env, jobject object,jint CheckCode){
	int w_nRet = -1;
	THREAD_LOCK();
	w_nRet = CommandProcess(CMD_SET_DUP_CHECK_CODE, CheckCode, 0, 0);
	THREAD_UNLOCK();
	if (ERR_SUCCESS != w_nRet) {
		LOG_D("setDuplicateCheck  failed!");
		return false;
	}
	LOG_D("setCheck success! CheckCode=%d",CheckCode);
	return true;
}

//获取指纹是否可重复
jint JNICALL  getDuplicateCheck(JNIEnv *env, jobject object){
	int CheckCode = -1;
	THREAD_LOCK();
	CheckCode = CommandProcess(CMD_GET_DUP_CHECK_CODE, 0, 0, 0);
	THREAD_UNLOCK();
	LOG_D("getCheck success! CheckCode=%d",CheckCode);
	return CheckCode;
}



//java方法与本地方法绑定
static const JNINativeMethod gMethods[] = {
		{ "initFP", "()Z", (void *) initFP },
		{ "stopFP", "()Z", (void *) stopFP },
		{ "deleteFP", "(J)Z",(void *) deleteFP },
		{ "deleteAllFP", "()Z",(void *) deleteAllFP },
		{ "readTemplate", "(J[B)Z",(void *) readTemplate },
		{ "writeTemplate", "(I[B)Z",(void *) writeTemplate },
		{ "getFingerImage", "([B[J)Z",(void *) getFingerImage },
		{ "identifyFP", "([J)Z",(void *) identifyFP },
		{ "getIdentifyImage", "()Z",(void *) getIdentifyImage },
		{ "enrollCompose", "(J)I",(void *) enrollCompose },
		{ "loopDetectFinger", "()Z",(void *) loopDetectFinger },
		{ "detectFinger", "()Z",(void *) detectFinger },
		{ "enrollTimes", "(JI)Z",(void *) enrollTimes },
		{ "getEnrollImage", "()Z",(void *) getEnrollImage },
		{ "enrollStart", "(J)Z",(void *) enrollStart },
		{ "getAvaliableId", "([J)Z",(void *) getAvaliableId },
		{ "setDuplicateCheck", "(I)Z",(void *) setDuplicateCheck },
		{ "getDuplicateCheck", "()I",(void *) getDuplicateCheck }
};

//为java中的某个类注册本地方法
static int registerNativeMethods(JNIEnv* env) {
	jclass clazz;
	static const char* const kClassName = "com/yn020/host/utils/FingerManager";	//为java中的哪个类注册本地方法
	clazz = env->FindClass(kClassName);
	if (clazz == NULL) {
		LOG_D("Can't find class %s\n", kClassName);
		return JNI_FALSE;

	}
	if (env->RegisterNatives(clazz, gMethods,
			sizeof(gMethods) / sizeof(gMethods[0])) != JNI_OK) {
		return JNI_FALSE;
	}
	return JNI_TRUE;

}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
	JNIEnv* env = NULL;
	jint result = -1;
	if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {	//从JavaVM获取JNIEnv，一般使用1.4的版本
		LOG_D("get env version failed!");
		return -1;
	}
	assert(env != NULL);
	if (!registerNativeMethods(env)) { //注册
		LOG_D("register native methods failed!");
		return -1;
	}
	result = JNI_VERSION_1_4;
	return result;

}

