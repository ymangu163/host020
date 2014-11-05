LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := fp_identify
LOCAL_SRC_FILES := libfp_identify.so
include $(PREBUILT_SHARED_LIBRARY)


include $(CLEAR_VARS)
LOCAL_ALLOW_UNDEFINED_SYMBOLS := true
LDFLAGS = -llog -shared 
LOCAL_C_INCLUDES := $(LOCAL_PATH)/include 
LOCAL_MODULE    := Finger
LOCAL_SRC_FILES := Finger.cpp
LOCAL_LDLIBS += -L$(SYSROOT)/usr/lib -llog
LOCAL_SHARED_LIBRARIES := libfp_identify
include $(BUILD_SHARED_LIBRARY)
