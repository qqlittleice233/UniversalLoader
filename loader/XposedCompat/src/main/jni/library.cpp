#include <jni.h>
#include "library.h"

constexpr int TYPE_VOID = 0;
constexpr int TYPE_JOBJECT = 1;
constexpr int TYPE_BOOLEAN = 2;
constexpr int TYPE_BYTE = 3;
constexpr int TYPE_CHAR = 4;
constexpr int TYPE_SHORT = 5;
constexpr int TYPE_INT = 6;
constexpr int TYPE_LONG = 7;
constexpr int TYPE_FLOAT = 8;
constexpr int TYPE_DOUBLE = 9;

JNIEXPORT JNICALL extern "C" jobject Java_art_qqlittleice_xposedcompat_util_NativeUtil_invokeSpecial(
    JNIEnv *env,
    jclass thiz,
    jclass superClz,
    jobject method,
    int callType,
    jobject obj,
    jobjectArray args
    ) {
    jmethodID methodID = env->FromReflectedMethod(method);
    jobject ret = nullptr;

    switch (callType) {
        case TYPE_VOID:
            env->CallNonvirtualVoidMethod(obj, superClz, methodID, args);
            break;
        case TYPE_JOBJECT:
            ret = env->CallNonvirtualObjectMethod(obj, superClz, methodID, args);
            break;
        case TYPE_BOOLEAN:
            ret = booleanToObject(env, env->CallNonvirtualBooleanMethod(obj, superClz, methodID, args));
            break;
        case TYPE_BYTE:
            ret = byteToObject(env, env->CallNonvirtualByteMethod(obj, superClz, methodID, args));
            break;
        case TYPE_CHAR:
            ret = charToObject(env, env->CallNonvirtualCharMethod(obj, superClz, methodID, args));
            break;
        case TYPE_SHORT:
            ret = shortToObject(env, env->CallNonvirtualShortMethod(obj, superClz, methodID, args));
            break;
        case TYPE_INT:
            ret = intToObject(env, env->CallNonvirtualIntMethod(obj, superClz, methodID, args));
            break;
        case TYPE_LONG:
            ret = longToObject(env, env->CallNonvirtualLongMethod(obj, superClz, methodID, args));
            break;
        case TYPE_FLOAT:
            ret = floatToObject(env, env->CallNonvirtualFloatMethod(obj, superClz, methodID, args));
            break;
        case TYPE_DOUBLE:
            ret = doubleToObject(env, env->CallNonvirtualDoubleMethod(obj, superClz, methodID, args));
            break;
        default:
            // throw an exception
            env->ThrowNew(env->FindClass("java/lang/IllegalArgumentException"), "Invalid call type");
    }
    return ret;
}

jobject booleanToObject(JNIEnv *env, jboolean boolean) {
    jclass booleanClass = env->FindClass("java/lang/Boolean");
    jmethodID constructor = env->GetMethodID(booleanClass, "<init>", "(Z)V");
    return env->NewObject(booleanClass, constructor, boolean);
}

jobject byteToObject(JNIEnv *env, jbyte byte) {
    jclass byteClass = env->FindClass("java/lang/Byte");
    jmethodID constructor = env->GetMethodID(byteClass, "<init>", "(B)V");
    return env->NewObject(byteClass, constructor, byte);
}

jobject charToObject(JNIEnv *env, jchar character) {
    jclass charClass = env->FindClass("java/lang/Character");
    jmethodID constructor = env->GetMethodID(charClass, "<init>", "(C)V");
    return env->NewObject(charClass, constructor, character);
}

jobject shortToObject(JNIEnv *env, jshort value) {
    jclass shortClass = env->FindClass("java/lang/Short");
    jmethodID constructor = env->GetMethodID(shortClass, "<init>", "(S)V");
    return env->NewObject(shortClass, constructor, value);
}

jobject intToObject(JNIEnv *env, jint value) {
    jclass intClass = env->FindClass("java/lang/Integer");
    jmethodID constructor = env->GetMethodID(intClass, "<init>", "(I)V");
    return env->NewObject(intClass, constructor, value);
}

jobject longToObject(JNIEnv *env, jlong value) {
    jclass longClass = env->FindClass("java/lang/Long");
    jmethodID constructor = env->GetMethodID(longClass, "<init>", "(J)V");
    return env->NewObject(longClass, constructor, value);
}

jobject floatToObject(JNIEnv *env, jfloat value) {
    jclass floatClass = env->FindClass("java/lang/Float");
    jmethodID constructor = env->GetMethodID(floatClass, "<init>", "(F)V");
    return env->NewObject(floatClass, constructor, value);
}

jobject doubleToObject(JNIEnv *env, jdouble value) {
    jclass doubleClass = env->FindClass("java/lang/Double");
    jmethodID constructor = env->GetMethodID(doubleClass, "<init>", "(D)V");
    return env->NewObject(doubleClass, constructor, value);
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    return JNI_VERSION_1_6;
}
