//
// Created by 乔兵 on 2018/3/8.
//

#include <jni.h>

#ifndef SHORTVIDEO_VIDEOPLAYER_H
#define SHORTVIDEO_VIDEOPLAYER_H
#ifdef __cplusplus
extern "C" {
#endif
JNIEXPORT jint JNICALL
Java_com_joe_shortvideo_util_NativeFunUtils_play(JNIEnv *env, jclass clazz, jobject surface);

#ifdef __cplusplus
}
#endif
#endif