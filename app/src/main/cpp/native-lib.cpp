#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_joe_shortvideo_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "通过三种方式绘制图片";
    return env->NewStringUTF(hello.c_str());
}
