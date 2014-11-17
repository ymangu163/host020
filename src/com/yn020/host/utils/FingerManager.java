package com.yn020.host.utils;


public class FingerManager {
	public static final int ERR_FAIL = 1;
	public static final int ERR_SUCCESS = 0;
	public static final int ERR_CONTINUE = 2;

	public static final int ERR_INIT_CMOS = 0x10;
	public static final int ERR_VERIFY = 0x11;
	public static final int ERR_IDENTIFY = 0x12;
	public static final int ERR_TMPL_EMPTY = 0x13;
	public static final int ERR_TMPL_NOT_EMPTY = 0x14;
	public static final int ERR_ALL_TMPL_EMPTY = 0x15;
	public static final int ERR_EMPTY_ID_NOEXIST = 0x16;
	public static final int ERR_BROKEN_ID_NOEXIST = 0x17;
	public static final int ERR_INVALID_TMPL_DATA = 0x18;
	public static final int ERR_DUPLICATION_ID = 0x19;
	public static final int ERR_TOO_FAST = 0x20;
	public static final int ERR_BAD_QUALITY = 0x21;
	public static final int ERR_SMALL_LINES = 0x22;
	public static final int ERR_GENERALIZE = 0x30;
	public static final int ERR_INTERNAL = 0x50;
	public static final int ERR_MEMORY = 0x51;
	public static final int ERR_EXCEPTION = 0x52;
	public static final int ERR_INVALID_TMPL_NO = 0x60;
	public static final int ERR_INVALID_SEC_VAL = 0x61;
	public static final int ERR_INVALID_BAUDRATE = 0x63;
	public static final int ERR_DEVICE_ID_EMPTY = 0x64;
	public static final int ERR_INVALID_DUP_VAL = 0x65;
	public static final int ERR_INVALID_PARAM = 0x70;
	public static final int GD_DETECT_FINGER = 0x71;
	public static final int GD_NO_DETECT_FINGER = 0x72;
	public static final int ERR_EMPTY_IMAGE = 0x73;
	public static final int ERR_DEVICE_NOT_OPEN = 0x74;
	public static final int ERR_SYS_DATA = 0x74;
	public static final int ERR_BAD_CHIP = 0x75;
	public static final int GD_TEMPLATE_NOT_EMPTY = 0x01;
	public static final int GD_TEMPLATE_EMPTY = 0x00;
	public static final int ERR_INCORRECT_COMMAND = 0xFF;

	public static final int CMD_INIT_FPLIB_CODE = 0x0100;
	public static final int CMD_VERIFY_CODE = 0x0101;
	public static final int CMD_IDENTIFY_CODE = 0x0102;
	public static final int CMD_ENROLL_CODE = 0x0103;
	public static final int CMD_ENROLL_ONETIME_CODE = 0x0104;
	public static final int CMD_CLEAR_TEMPLATE_CODE = 0x0105;
	public static final int CMD_CLEAR_ALLTEMPLATE_CODE = 0x0106;
	public static final int CMD_GET_EMPTY_ID_CODE = 0x0107;
	public static final int CMD_GET_TEMPLATE_STATUS_CODE = 0x0108;
	public static final int CMD_GET_BROKEN_TEMPLATE_CODE = 0x0109;
	public static final int CMD_READ_TEMPLATE_CODE = 0x010A;
	public static final int CMD_WRITE_TEMPLATE_CODE = 0x010B;
	public static final int CMD_SET_SECURITYLEVEL_CODE = 0x010C;
	public static final int CMD_GET_SECURITYLEVEL_CODE = 0x010D;
	public static final int CMD_GET_FW_VERSION_CODE = 0x0112;
	public static final int CMD_FINGER_DETECT_CODE = 0x0113;
	public static final int CMD_SET_DUP_CHECK_CODE = 0x0115;
	public static final int CMD_GET_DUP_CHECK_CODE = 0x0116;
	public static final int CMD_GET_IMAGE_CODE = 0x0125;
	public static final int CMD_GET_ENROLL_COUNT_CODE = 0x0126;
	public static final int CMD_SLED_CONTROL_CODE = 0x0127;
	public static final int CMD_READ_CAM_REG_CODE = 0x0128;
	public static final int CMD_WRITE_CAM_REG_CODE = 0x0129;
	public static final int CMD_SET_AUTO_LEARN_CODE = 0x012A;
	public static final int CMD_READ_LICENSE_CODE = 0x0181;
	public static final int CMD_READ_SENSOR_PARAM_CODE = 0x0190;
	public static final int CMD_WRITE_SENSOR_PARAM_CODE = 0x0191;
	public static final int CMD_ADJUST_BRIGHT_CODE = 0x0196;
	public static final int CMD_SET_BRIGHT_CODE = 0x0197;
	public static final int CMD_GET_FRAME_CODE = 0x0198;
	public static final int CMD_STOP_FPLIB_CODE = 0xaaaa;

	public static final int MAX_FINGER_IMAGE_W = 640;
	public static final int MAX_FINGER_IMAGE_H = 480;
	
	/**
	 * 完成一个手指注册所需的指纹录入次数
	 */
	public static final int EnrollFP_Needed_InputCount =  3;
	/**
	 * 每位员工可以录入的最大手指数目
	 */
	public static final int MaxEnrolledFPNum_PerEmployee = 3;
	/** * 指纹模板的最大字节数 */
	public static final int MaxFingerTemplateSize = 498;
	
	/////////////////////////////////////////////////////////////////////////
	static {
		System.loadLibrary("fp_identify");
		System.loadLibrary("Finger");
	}
	
	///////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	
	
	
	
	private Thread  fingerDetecThread ;						//用来检测指纹的线程
	
	
	private  FingerManager() {  //构造方法设为私有，单例模式
		
	}

	private static Object fingerDetecThreadLock = new Object();  //检测指纹的线程的锁

	private boolean needNextCapture = false;
	private static Object fingerDetected_LinstenerLock = new Object(); //检测是否有指纹的锁
	
	private static FingerManager fingerManager = null;			//单例
	private static Object fingerManagerLock = new Object();		//同步锁对象
	private boolean bInited = false;
	
	
	

	/**
	 * 更改”是否需要继续检测指纹“标志，设为false后不会再回调结果
	 * 给fingerDetected_Linstener 直到为true，用于FingerMechine
	 * 使用者"不想通过重设fingerDetected_Linstener来暂停指纹检测"的情形
	 * @param needNextCapture
	 */
	public void setNeedNextCapture(boolean needNextCapture)
	{
		this.needNextCapture = needNextCapture;
	}
	
	
	//获取FingerMechine单例对象
	public static synchronized  FingerManager getSharedInstance(){
		if(fingerManager==null){
			fingerManager=new FingerManager();
		}
		return fingerManager;
	}
	

	
	public boolean FPM_initFP(){
		synchronized (fingerManagerLock){
			return initFP();
		}		
	}
	
	public boolean FPM_stopFP(){
		synchronized (fingerManagerLock){
			return stopFP();
		}		
	}
	
	
	
	
	//开始注册指纹
	public boolean FPM_enrollFP_Start(long enrollId){
		synchronized (fingerManagerLock) {
			return  enrollStart(enrollId);
		}
		
	}
	
	//第几次注册指纹
	public boolean FPM_enrollId_Times(long enrollId,int time){
		synchronized (fingerManagerLock) {
			return enrollTimes(enrollId, time);
		}
	}
	//指纹合成，将前面dupNumber[0]个输入指纹合成并注册到enrollId
	public int FPM_enrollCompose(long enrollId){
		synchronized (fingerManagerLock) {
			return enrollCompose(enrollId);
		}
		
	}
	
	//检索没被利用的可使用 ID号
	public boolean FPM_getAvailableId(long[] id){
		synchronized (fingerManagerLock) {
			return getAvaliableId(id);
		}	
	}
	
	//删除一个已注册指纹数据
	public boolean FPM_deleteFP(long id){
		synchronized (fingerManagerLock) {
			return deleteFP(id);
		}
		
	}
	
	//删除所有已注册指纹数据
	public  boolean FPM_deleteAllFP(){
		synchronized (fingerManagerLock) {
			return deleteAllFP();
		}
		
	}
	
	//识别当前指纹并进行1:N对比
	public boolean FPM_identifyFP(long [] id){
		synchronized (fingerManagerLock) {
			return identifyFP(id);
		}
		
		
	}
	//检测是否有手指按下，true为按下，false为没有手指按下
	public boolean FPM_detectFinger(){
		synchronized (fingerManagerLock) {
			return detectFinger();
		}
	}
	
	//检测是否松开手指,true为松开
		public boolean FPM_loopDetectFinger(){
			synchronized (fingerManagerLock) {
				return loopDetectFinger();
			}
		}
	
	
	
	
	
	
	// *** 写入指定id的指纹模板*//
	public boolean FPM_writeTemplate(int id, byte[] templateBytes){
		synchronized (fingerManagerLock) {
			return writeTemplate(id, templateBytes);
		}
	}
	
	// *** 读取指定id的指纹模板*//
	public boolean FPM_readTemplate(long id, byte[] templateBytes){
		synchronized (fingerManagerLock) {
			return readTemplate(id,templateBytes);
		}		
	}
	//获得注册图像
	public boolean FPM_getEnrollImage(){
		synchronized (fingerManagerLock) {
			return getEnrollImage();
		}			
	}
	//获得识别图像
	public boolean FPM_getIdentifyImage(){
		synchronized (fingerManagerLock) {
			return getIdentifyImage();
		}		
		
	}
	
	// 获取指纹图像
	public boolean FPM_getFingerImage(byte[] imageByte, long[] imageSize) {
		synchronized (fingerManagerLock) {
			return getFingerImage(imageByte, imageSize);
		}
	}
	
	
	//设置指纹是否可重复
	public  boolean FPM_setDuplicateCheck(int CheckCode){
		synchronized (fingerManagerLock) {
			return setDuplicateCheck(CheckCode);
		}				
	}
	
	//得到指纹是否可重复参数
	public  int FPM_getDuplicateCheck(){
		synchronized (fingerManagerLock) {
			return getDuplicateCheck();
		}				
	}
	
	//设置指纹是否可重复
	public  boolean FPM_setSecurityLevel(int level){
		synchronized (fingerManagerLock) {
			return setSecurityLevel(level);
		}				
	}
	
	//得到指纹是否可重复参数
	public  int FPM_getSecurityLevel(){
		synchronized (fingerManagerLock) {
			return getSecurityLevel();
		}				
	}
	
	
	
	
	
/////////////////////////////////////////////////////////////////////////////
	// 启动时必须调用此指令。并且调用之前,务必初始化
		public native boolean initFP();
		
		public native boolean stopFP();

		// 开始注册
		public native boolean enrollStart(long enrollId);

		// 第*次指纹录入
		public native boolean enrollTimes(long enrollId, int time);

		// 指纹合成
		public native int enrollCompose(long enrollId);

		// 获取注册图像
		public native boolean getEnrollImage();

		// 检索没被利用的可使用 ID号
		public native boolean getAvaliableId(long[] id);

		// 删除一个已注册指纹数据
		public native boolean deleteFP(long id);

		// 删除所有已注册指纹数据
		public native boolean deleteAllFP();

		// 检测当前是否有手指按下
		public native boolean detectFinger();
		// 检测当前是否松开手指
		public native boolean loopDetectFinger();

		// 从Sensor采集指纹图像
		public native boolean captureFingerImage(byte[] imageBinaryData,
				long[] imageSize);

		// 已登记的所有 template 与利用 CMD_GET_IMAG 指令生成的template之间进行 1:N比对。
		public native boolean identifyFP(long[] id);

		// 获取识别图像
		public native boolean getIdentifyImage();

		// *** 读取指定id的指纹模板*//
		public native boolean readTemplate(long id, byte[] templateBytes);

		// *** 写入指定id的指纹模板*//
		public native boolean writeTemplate(int id, byte[] templateBytes);

		// 获取指纹图像
		public native boolean getFingerImage(byte[] imageByte, long[] imageSize);	
		
		//设置指纹是否可重复
		public native boolean setDuplicateCheck(int CheckCode);	
		
		//得到指纹是否可重复参数
		public native int getDuplicateCheck();	
		
		//设置安全等级
		public native boolean setSecurityLevel(int level);	
		
		//得到安全等级参数
		public native int getSecurityLevel();	
		
		//设置auto_learn
		public native boolean setAutoLearn(boolean autoLearn);	
		
		//得到auto_learn参数
		public native int getAutoLearn();	
		
		
		
		
	
/////////////////////////////////////////////////////////////////////////////	
		
		
}
