package art.qqlittleice.xposedcompat.util

import art.qqlittleice.xposedcompat.transition.bridge.BridgeApi
import org.luckypray.dexkit.util.DexSignUtil
import java.lang.reflect.Method
import java.lang.reflect.Modifier

fun BridgeApi.HookerCallback<Method>.invokeSuper(vararg args: Any?): Any? {
    val method = this.getExecutable()
    if (method.modifiers and Modifier.STATIC != 0) {
        throw IllegalAccessException("Cannot invoke super method for static method ${method.name} in ${method.declaringClass.name}.")
    }

    var isOverride = false
    var superClazz: Class<*>? = method.declaringClass.superclass
    var superMethod: Method? = null
    while (superClazz != null) {
        try {
            superMethod = superClazz.getDeclaredMethod(method.name, *method.parameterTypes)
            isOverride = true
            break
        } catch (_: NoSuchMethodException) {}
        superClazz = superClazz.superclass
    }

    if (!isOverride) {
        throw IllegalAccessException("Cannot invoke super method for ${method.name} in ${method.declaringClass.name}, no overridden method found.")
    }
    superMethod ?: throw RuntimeException("???")

    val methodSign = DexSignUtil.getMethodSign(method)
    val callType = when(DexSignUtil.getTypeSign(method.returnType)) {
        "V" -> 0 // void
        "Z" -> 2 // boolean
        "B" -> 3 // byte
        "C" -> 4 // char
        "S" -> 5 // short
        "I" -> 6 // int
        "L" -> 7 // long
        "F" -> 8 // float
        "D" -> 9 // double
        else -> 1 // Object
    }
    return NativeUtil.invokeSpecial(superClazz, superMethod.name, methodSign, callType, getThisObject(), *args)
}
