LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := MagicBeautify
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	/Users/qiaobing/Downloads/AAVT-master/magicfilter/src/main/jni/beautify/MagicBeautify.cpp \
	/Users/qiaobing/Downloads/AAVT-master/magicfilter/src/main/jni/bitmap/BitmapOperation.cpp \
	/Users/qiaobing/Downloads/AAVT-master/magicfilter/src/main/jni/bitmap/Conversion.cpp \
	/Users/qiaobing/Downloads/AAVT-master/magicfilter/src/main/jni/MagicJni.cpp \

LOCAL_C_INCLUDES += /Users/qiaobing/Downloads/AAVT-master/magicfilter/src/main/jni
LOCAL_C_INCLUDES += /Users/qiaobing/Downloads/AAVT-master/magicfilter/src/release/jni

include $(BUILD_SHARED_LIBRARY)
