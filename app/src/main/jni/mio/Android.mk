LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_CPP_EXTENSION := .cpp .cc
LOCAL_MODULE    := Mio
LOCAL_SRC_FILES := Mio.cpp

include $(BUILD_SHARED_LIBRARY)
