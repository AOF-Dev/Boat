
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <boat.h>

ANativeWindow* boatGetNativeWindow() {
    return mBoat.window;
}

void* boatGetNativeDisplay() {
    return mBoat.display;
}

void boatGetCurrentEvent(BoatInputEvent* event) {
    memcpy(event, &mBoat.current_event, sizeof(BoatInputEvent));
}

void boatSetCurrentEventProcessor(BoatEventProcessor processor) {
    mBoat.current_event_processor = processor;
}

void boatSetCursorMode(int mode) {
    if (mBoat.android_jvm == 0) {
        return;
    }
    JNIEnv* env = 0;

    jint result = (*mBoat.android_jvm)->AttachCurrentThread(mBoat.android_jvm, &env, 0);

    if (result != JNI_OK || env == 0) {
        __android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to attach thread to JavaVM.");
        abort();
    }

    jclass class_BoatInput = mBoat.class_BoatInput;

    if (class_BoatInput == 0) {
        __android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to find class: cosine/boat/BoatInput.");
        abort();
    }

    jmethodID BoatInput_setCursorMode = (*env)->GetStaticMethodID(env, class_BoatInput, "setCursorMode", "(I)V");

    if (BoatInput_setCursorMode == 0) {
        __android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to find static method BoatInput::setCursorMode");
        abort();
    }
    (*env)->CallStaticVoidMethod(env, class_BoatInput, BoatInput_setCursorMode, mode);

    (*mBoat.android_jvm)->DetachCurrentThread(mBoat.android_jvm);
}

void boatSetCursorPos(int x, int y) {
    /*
    if (mBoat.android_jvm == 0){
        return;
    }
    JNIEnv* env = 0;
    
    jint result = (*mBoat.android_jvm)->AttachCurrentThread(mBoat.android_jvm, &env, 0);
    
    if (result != JNI_OK || env == 0){
        __android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to attach thread to JavaVM.");
        abort();
    }
    
    jclass class_BoatInput = mBoat.class_BoatInput;
    
    if (class_BoatInput == 0){
        __android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to find class: cosine/boat/BoatInput.");
        abort();
    }
    
    jmethodID BoatInput_setCursorPos = (*env)->GetStaticMethodID(env, class_BoatInput, "setCursorPos", "(II)V");
    
    if (BoatInput_setCursorPos == 0){
        __android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to find static method BoatInput::setCursorPos");
        abort();
    }
    (*env)->CallStaticVoidMethod(env, class_BoatInput, BoatInput_setCursorPos, x, y);
    
    
    (*mBoat.android_jvm)->DetachCurrentThread(mBoat.android_jvm);
    */
}

void boatSetPrimaryClipString(const char* string) {
    if (mBoat.android_jvm == 0) {
        return;
    }
    JNIEnv* env = 0;

    jint result = (*mBoat.android_jvm)->AttachCurrentThread(mBoat.android_jvm, &env, 0);
    
    if (result != JNI_OK || env == 0) {
        __android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to attach thread to JavaVM.");
        abort();
    }

    jclass class_BoatInput = mBoat.class_BoatInput;
    
    if (class_BoatInput == 0) {
        __android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to find class: cosine/boat/BoatInput.");
        abort();
    }

    jmethodID BoatInput_setPrimaryClipString = (*env)->GetStaticMethodID(env, class_BoatInput, "setPrimaryClipString", "(Ljava/lang/String;)V");
    
    if (BoatInput_setPrimaryClipString == 0) {
        __android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to find static method BoatInput::setPrimaryClipString");
        abort();
    }
    (*env)->CallStaticVoidMethod(env, class_BoatInput, BoatInput_setPrimaryClipString, (*env)->NewStringUTF(env, string));

    (*mBoat.android_jvm)->DetachCurrentThread(mBoat.android_jvm);
}

const char* boatGetPrimaryClipString() {
    if (mBoat.android_jvm == 0){
        return NULL;
    }
    JNIEnv* env = 0;

    jint result = (*mBoat.android_jvm)->AttachCurrentThread(mBoat.android_jvm, &env, 0);

    if (result != JNI_OK || env == 0) {
        __android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to attach thread to JavaVM.");
        abort();
    }

    jclass class_BoatInput = mBoat.class_BoatInput;

    if (class_BoatInput == 0) {
        __android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to find class: cosine/boat/BoatInput.");
        abort();
    }

    jmethodID BoatInput_getPrimaryClipString = (*env)->GetStaticMethodID(env, class_BoatInput, "getPrimaryClipString", "()Ljava/lang/String;");

    if (BoatInput_getPrimaryClipString == 0) {
        __android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to find static method BoatInput::getPrimaryClipString");
        abort();
    }

    if (mBoat.clipboard_string != NULL) {
        free(mBoat.clipboard_string);
        mBoat.clipboard_string = NULL;
    }

    jstring clipstr = (jstring)(*env)->CallStaticObjectMethod(env, class_BoatInput, BoatInput_getPrimaryClipString);

    const char* string = NULL;
    if (clipstr != NULL) {
        string = (*env)->GetStringUTFChars(env, clipstr, NULL);
        if (string != NULL) {
            mBoat.clipboard_string = strdup(string);
        }
    }

    (*mBoat.android_jvm)->DetachCurrentThread(mBoat.android_jvm);

    return mBoat.clipboard_string;
}

JNIEXPORT void JNICALL Java_cosine_boat_BoatInput_send(JNIEnv* env, jclass clazz, jlong time, jint type, jint p1, jint p2) {

    mBoat.current_event.time = time;
    mBoat.current_event.type = type;

    if (type == ButtonPress || type == ButtonRelease) {
        mBoat.current_event.mouse_button = p1;
    }
    else if (type == KeyPress || type == KeyRelease) {
        mBoat.current_event.keycode = p1;
        mBoat.current_event.keychar = p2;
    }
    else if (type == MotionNotify) {
        mBoat.current_event.x = p1;
        mBoat.current_event.y = p2;
    }

    if (mBoat.current_event_processor != 0) {
        mBoat.current_event_processor();
    }

}

JNIEXPORT void JNICALL Java_cosine_boat_BoatActivity_setBoatNativeWindow(JNIEnv* env, jclass clazz, jobject surface) {
    mBoat.window = ANativeWindow_fromSurface(env, surface);
    __android_log_print(ANDROID_LOG_ERROR, "Boat", "setBoatNativeWindow : %p", mBoat.window);
    mBoat.display = 0;
}


JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved) {

    mBoat.android_jvm = vm;

    JNIEnv* env = 0;

    jint result = (*mBoat.android_jvm)->AttachCurrentThread(mBoat.android_jvm, &env, 0);

    if (result != JNI_OK || env == 0) {
        __android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to attach thread to JavaVM.");
        abort();
    }

    jclass class_BoatInput = (*env)->FindClass(env, "cosine/boat/BoatInput");
    if (class_BoatInput == 0) {
        __android_log_print(ANDROID_LOG_ERROR, "Boat", "Failed to find class: cosine/boat/BoatInput.");
        abort();
    }
    mBoat.class_BoatInput = (jclass)(*env)->NewGlobalRef(env, class_BoatInput);

    return JNI_VERSION_1_6;
}
