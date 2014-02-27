LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := libtest
LOCAL_SRC_FILES := hook.c
LOCAL_C_INCLUDES := /home/rorist/android/adbi/instruments/base/ /home/rorist/android/ddi/dalvikhook/jni/
LOCAL_LDLIBS    := -L/home/rorist/android/ddi/dalvikhook/jni/libs -ldl -ldvm
LOCAL_LDLIBS    := -Wl,--start-group /home/rorist/android/adbi/instruments/base/obj/local/armeabi/libbase.a /home/rorist/android/ddi/dalvikhook/obj/local/armeabi/libdalvikhook.a -Wl,--end-group
LOCAL_CFLAGS    := -g -w

include $(BUILD_SHARED_LIBRARY)

