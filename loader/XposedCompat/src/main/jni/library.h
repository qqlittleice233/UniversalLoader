#ifndef ANDROID_LIBRARY_LIBRARY_H
#define ANDROID_LIBRARY_LIBRARY_H

jobject booleanToObject(JNIEnv *env, jboolean boolean);
jobject byteToObject(JNIEnv *env, jbyte byte);
jobject charToObject(JNIEnv *env, jchar character);
jobject shortToObject(JNIEnv *env, jshort value);
jobject intToObject(JNIEnv *env, jint value);
jobject longToObject(JNIEnv *env, jlong value);
jobject floatToObject(JNIEnv *env, jfloat value);
jobject doubleToObject(JNIEnv *env, jdouble value);

#endif //ANDROID_LIBRARY_LIBRARY_H