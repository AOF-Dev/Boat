
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := boat
LOCAL_SRC_FILES := boat.c loadme.c boat_activity.c
LOCAL_LDLIBS    := -llog -ldl -landroid
TARGET_NO_UNDEFINED_LDFLAGS := 
LOCAL_CFLAGS += -mthumb -O2 -std=gnu99
include $(BUILD_SHARED_LIBRARY)


