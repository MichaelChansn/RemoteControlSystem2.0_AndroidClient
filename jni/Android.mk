 LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := JNIBtmProcess
LOCAL_SRC_FILES := JNIBtmProcess.cpp
LOCAL_LDLIBS := -llog
LOCAL_LDFLAGS += -ljnigraphics

include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE    := LZOjni
LOCAL_SRC_FILES := LZOjni.c minilzo.c

include $(BUILD_SHARED_LIBRARY)