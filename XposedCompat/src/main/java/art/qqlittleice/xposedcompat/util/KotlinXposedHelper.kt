package art.qqlittleice.xposedcompat.util

import art.qqlittleice.xposedcompat.transition.BridgeApi
import art.qqlittleice.xposedcompat.transition.UniversalBridge
import java.lang.reflect.Constructor
import java.lang.reflect.Executable
import java.lang.reflect.Method

fun Class<*>.hookMethod(methodName: String, vararg args: Any?) = try {
    if (args.last() !is BridgeApi.Hooker<*>) throw IllegalArgumentException("The last argument must be a Hooker")
    val theHooker = args.last() as BridgeApi.Hooker<Method>
    val clazzArgs = args.dropLast(1)
    if (clazzArgs.isNotEmpty() and clazzArgs.all { (it == null) or (it !is Class<*>) }) throw IllegalArgumentException("The arguments must be Class<?> or not null")
    val method = declaredMethods.firstOrNull { method ->
        (method.name == methodName) and (method.parameterCount == clazzArgs.size) and (method.parameterTypes.zip(clazzArgs).all { (type, arg) -> type == arg })
    } ?: throw NoSuchMethodException("No such method $methodName with ${clazzArgs.size} parameters (${clazzArgs.joinToString { it.toString() }})")
    UniversalBridge.get().hook(method, theHooker)
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking method", e)
    null
}

fun <T: Executable> Executable.hook( hooker: BridgeApi.Hooker<T>) = try {
    when (this) {
        is Method -> UniversalBridge.get().hook(this, hooker as BridgeApi.Hooker<Method>)
        is Constructor<*> -> UniversalBridge.get().hook(this, hooker as BridgeApi.Hooker<Constructor<*>>)
        else -> throw IllegalArgumentException("???")
    }
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking method", e)
    null
}

inline fun <T: Executable> Executable.replace( crossinline hooker: (BridgeApi.BeforeMethodInvokedCallback<T>) -> Any?) = try {
    when (this) {
        is Method -> UniversalBridge.get().hook(this, object : BridgeApi.Hooker<Method> {
            override fun beforeMethodInvoked(callback: BridgeApi.BeforeMethodInvokedCallback<Method>) {
                runCatching {
                    hooker(callback as BridgeApi.BeforeMethodInvokedCallback<T>)
                }.onSuccess { callback.returnAndSkip(it) }.onFailure { callback.throwAndSkip(it) }
            }
            override fun afterMethodInvoked(callback: BridgeApi.AfterMethodInvokedCallback<Method>) {}
        })
        is Constructor<*> -> UniversalBridge.get().hook(this, object : BridgeApi.Hooker<Constructor<*>> {
            override fun beforeMethodInvoked(callback: BridgeApi.BeforeMethodInvokedCallback<Constructor<*>>) {
                runCatching {
                    hooker(callback as BridgeApi.BeforeMethodInvokedCallback<T>)
                }.onSuccess { callback.returnAndSkip(it) }.onFailure { callback.throwAndSkip(it) }
            }
            override fun afterMethodInvoked(callback: BridgeApi.AfterMethodInvokedCallback<Constructor<*>>) {}
        })
        else -> throw IllegalArgumentException("???")
    }
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking method", e)
    null
}

inline fun <T: Executable> Executable.hookBefore( crossinline hooker: (BridgeApi.BeforeMethodInvokedCallback<T>) -> Unit) = try {
    when (this) {
        is Method -> UniversalBridge.get().hook(this, object : BridgeApi.Hooker<Method> {
            override fun beforeMethodInvoked(callback: BridgeApi.BeforeMethodInvokedCallback<Method>) {
                hooker(callback as BridgeApi.BeforeMethodInvokedCallback<T>)
            }
            override fun afterMethodInvoked(callback: BridgeApi.AfterMethodInvokedCallback<Method>) {}
        })
        is Constructor<*> -> UniversalBridge.get().hook(this, object : BridgeApi.Hooker<Constructor<*>> {
            override fun beforeMethodInvoked(callback: BridgeApi.BeforeMethodInvokedCallback<Constructor<*>>) {
                hooker(callback as BridgeApi.BeforeMethodInvokedCallback<T>)
            }
            override fun afterMethodInvoked(callback: BridgeApi.AfterMethodInvokedCallback<Constructor<*>>) {}
        })
        else -> throw IllegalArgumentException("???")
    }
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking method", e)
    null
}

inline fun <T: Executable> Executable.hookAfter( crossinline hooker: (BridgeApi.AfterMethodInvokedCallback<T>) -> Unit) = try {
    when (this) {
        is Method -> UniversalBridge.get().hook(this, object : BridgeApi.Hooker<Method> {
            override fun beforeMethodInvoked(callback: BridgeApi.BeforeMethodInvokedCallback<Method>) {}
            override fun afterMethodInvoked(callback: BridgeApi.AfterMethodInvokedCallback<Method>) {
                hooker(callback as BridgeApi.AfterMethodInvokedCallback<T>)
            }
        })
        is Constructor<*> -> UniversalBridge.get().hook(this, object : BridgeApi.Hooker<Constructor<*>> {
            override fun beforeMethodInvoked(callback: BridgeApi.BeforeMethodInvokedCallback<Constructor<*>>) {}
            override fun afterMethodInvoked(callback: BridgeApi.AfterMethodInvokedCallback<Constructor<*>>) {
                hooker(callback as BridgeApi.AfterMethodInvokedCallback<T>)
            }
        })
        else -> throw IllegalArgumentException("???")
    }
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking method", e)
    null
}

inline fun Class<*>.hookBeforeMethod(methodName: String,  vararg args: Class<*>, crossinline hooker: (BridgeApi.BeforeMethodInvokedCallback<Method>) -> Unit) = try {
    hookMethod(methodName, *args, object : BridgeApi.Hooker<Method> {
        override fun beforeMethodInvoked(callback: BridgeApi.BeforeMethodInvokedCallback<Method>) {
            hooker.invoke(callback)
        }
        override fun afterMethodInvoked(callback: BridgeApi.AfterMethodInvokedCallback<Method>) {}
    })
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking method", e)
    null
}

inline fun Class<*>.hookAfterMethod(methodName: String,  vararg args: Class<*>, crossinline hooker: (BridgeApi.AfterMethodInvokedCallback<Method>) -> Unit) = try {
    hookMethod(methodName, *args, object : BridgeApi.Hooker<Method> {
        override fun beforeMethodInvoked(callback: BridgeApi.BeforeMethodInvokedCallback<Method>) {}
        override fun afterMethodInvoked(callback: BridgeApi.AfterMethodInvokedCallback<Method>) {
            hooker.invoke(callback)
        }
    })
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking method", e)
    null
}

inline fun Class<*>.replaceMethod(methodName: String,  vararg args: Class<*>, crossinline hooker: (BridgeApi.BeforeMethodInvokedCallback<Method>) -> Any?) = try {
    hookMethod(methodName, *args, object : BridgeApi.Hooker<Method> {
        override fun beforeMethodInvoked(callback: BridgeApi.BeforeMethodInvokedCallback<Method>) {
            runCatching {
                hooker.invoke(callback)
            }.onSuccess { callback.returnAndSkip(it) }.onFailure { callback.throwAndSkip(it) }
        }
        override fun afterMethodInvoked(callback: BridgeApi.AfterMethodInvokedCallback<Method>) {}
    })
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking method", e)
    null
}

fun Class<*>.hookAllMethods(methodName: String,  hooker: BridgeApi.Hooker<Method>) = try {
    UniversalBridge.get().hookAllMethods(this, methodName, hooker)
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking method", e)
    emptyList()
}

inline fun Class<*>.hookBeforeAllMethods(methodName: String,  crossinline hooker: (BridgeApi.BeforeMethodInvokedCallback<Method>) -> Unit) = try {
    UniversalBridge.get().hookAllMethods(this, methodName, object : BridgeApi.Hooker<Method> {
        override fun beforeMethodInvoked(callback: BridgeApi.BeforeMethodInvokedCallback<Method>) {
            hooker.invoke(callback)
        }
        override fun afterMethodInvoked(callback: BridgeApi.AfterMethodInvokedCallback<Method>) {}
    })
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking method", e)
    emptyList()
}

inline fun Class<*>.hookAfterAllMethods(methodName: String,  crossinline hooker: (BridgeApi.AfterMethodInvokedCallback<Method>) -> Unit) = try {
    UniversalBridge.get().hookAllMethods(this, methodName, object : BridgeApi.Hooker<Method> {
        override fun beforeMethodInvoked(callback: BridgeApi.BeforeMethodInvokedCallback<Method>) {}
        override fun afterMethodInvoked(callback: BridgeApi.AfterMethodInvokedCallback<Method>) {
            hooker.invoke(callback)
        }
    })
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking method", e)
    emptyList()
}

inline fun Class<*>.replaceAllMethods(methodName: String,  crossinline hooker: (BridgeApi.BeforeMethodInvokedCallback<Method>) -> Any?) = try {
    UniversalBridge.get().hookAllMethods(this, methodName, object : BridgeApi.Hooker<Method> {
        override fun beforeMethodInvoked(callback: BridgeApi.BeforeMethodInvokedCallback<Method>) {
            runCatching {
                hooker.invoke(callback)
            }.onSuccess { callback.returnAndSkip(it) }.onFailure { callback.throwAndSkip(it) }
        }
        override fun afterMethodInvoked(callback: BridgeApi.AfterMethodInvokedCallback<Method>) {}
    })
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking method", e)
    emptyList()
}

fun Class<*>.hookConstructor( vararg args: Any?) = try {
    if (args.last() !is BridgeApi.Hooker<*>) throw IllegalArgumentException("The last argument must be a Hooker")
    val theHooker = args.last() as BridgeApi.Hooker<Constructor<*>>
    val clazzArgs = args.dropLast(1)
    if (clazzArgs.isNotEmpty() and clazzArgs.all { (it == null) or (it !is Class<*>) }) throw IllegalArgumentException("The arguments must be Class<?> or not null")
    val constructor = declaredConstructors.firstOrNull { constructor ->
        (constructor.parameterCount == clazzArgs.size) and (constructor.parameterTypes.zip(clazzArgs).all { (type, arg) -> type == arg })
    } ?: throw NoSuchMethodException("No such constructor with ${clazzArgs.size} parameters (${clazzArgs.joinToString { it.toString() }})")
    UniversalBridge.get().hook(constructor, theHooker)
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking constructor", e)
    null
}

inline fun Class<*>.hookBeforeConstructor( vararg args: Class<*>, crossinline hook: (BridgeApi.BeforeMethodInvokedCallback<Constructor<*>>) -> Unit) = try {
    hookConstructor(*args, object : BridgeApi.Hooker<Constructor<*>> {
        override fun beforeMethodInvoked(callback: BridgeApi.BeforeMethodInvokedCallback<Constructor<*>>) {
            hook.invoke(callback)
        }
        override fun afterMethodInvoked(callback: BridgeApi.AfterMethodInvokedCallback<Constructor<*>>) {}
    })
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking constructor", e)
    null
}

inline fun Class<*>.hookAfterConstructor( vararg args: Class<*>, crossinline hook: (BridgeApi.AfterMethodInvokedCallback<Constructor<*>>) -> Unit) = try {
    hookConstructor(*args, object : BridgeApi.Hooker<Constructor<*>> {
        override fun beforeMethodInvoked(callback: BridgeApi.BeforeMethodInvokedCallback<Constructor<*>>) {}
        override fun afterMethodInvoked(callback: BridgeApi.AfterMethodInvokedCallback<Constructor<*>>) {
            hook.invoke(callback)
        }
    })
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking constructor", e)
    null
}

inline fun Class<*>.replaceConstructor( vararg args: Class<*>, crossinline hook: (BridgeApi.BeforeMethodInvokedCallback<Constructor<*>>) -> Any?) = try {
    hookConstructor(*args, object : BridgeApi.Hooker<Constructor<*>> {
        override fun beforeMethodInvoked(callback: BridgeApi.BeforeMethodInvokedCallback<Constructor<*>>) {
            runCatching {
                hook.invoke(callback)
            }.onSuccess { callback.returnAndSkip(it) }.onFailure { callback.throwAndSkip(it) }
        }
        override fun afterMethodInvoked(callback: BridgeApi.AfterMethodInvokedCallback<Constructor<*>>) {}
    })
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking constructor", e)
    null
}

fun Class<*>.hookAllConstructor( hooker: BridgeApi.Hooker<Constructor<*>>) = try {
    UniversalBridge.get().hookAllConstructors(this, hooker)
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking constructor", e)
    emptyList()
}

inline fun Class<*>.hookBeforeAllConstructor( crossinline hook: (BridgeApi.BeforeMethodInvokedCallback<Constructor<*>>) -> Unit) = try {
    UniversalBridge.get().hookAllConstructors(this, object : BridgeApi.Hooker<Constructor<*>> {
        override fun beforeMethodInvoked(callback: BridgeApi.BeforeMethodInvokedCallback<Constructor<*>>) {
            hook.invoke(callback)
        }
        override fun afterMethodInvoked(callback: BridgeApi.AfterMethodInvokedCallback<Constructor<*>>) {}
    })
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking constructor", e)
    emptyList()
}

inline fun Class<*>.hookAfterAllConstructor( crossinline hook: (BridgeApi.AfterMethodInvokedCallback<Constructor<*>>) -> Unit) = try {
    UniversalBridge.get().hookAllConstructors(this, object : BridgeApi.Hooker<Constructor<*>> {
        override fun beforeMethodInvoked(callback: BridgeApi.BeforeMethodInvokedCallback<Constructor<*>>) {}
        override fun afterMethodInvoked(callback: BridgeApi.AfterMethodInvokedCallback<Constructor<*>>) {
            hook.invoke(callback)
        }
    })
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking constructor", e)
    emptyList()
}

fun String.hookMethod(classLoader: ClassLoader, methodName: String,  vararg args: Any?) = try {
    val clazz = classLoader.loadClass(this)
    clazz.hookMethod(methodName, *args)
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking method", e)
    null
}

fun String.hookMethod(classLoader: ClassLoader, methodName: String,  vararg args: Class<*>, hooker: BridgeApi.Hooker<Method>) = try {
    val clazz = classLoader.loadClass(this)
    clazz.hookMethod(methodName, *args, hooker)
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking method", e)
    null
}

fun String.hookBeforeMethod(classLoader: ClassLoader, methodName: String,  vararg args: Class<*>, hooker: (BridgeApi.BeforeMethodInvokedCallback<Method>) -> Unit) = try {
    val clazz = classLoader.loadClass(this)
    clazz.hookBeforeMethod(methodName, *args, hooker = hooker)
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking method", e)
    null
}

fun String.hookAfterMethod(classLoader: ClassLoader, methodName: String,  vararg args: Class<*>, hooker: (BridgeApi.AfterMethodInvokedCallback<Method>) -> Unit) = try {
    val clazz = classLoader.loadClass(this)
    clazz.hookAfterMethod(methodName, *args, hooker = hooker)
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking method", e)
    null
}

fun String.replaceMethod(classLoader: ClassLoader, methodName: String, vararg args: Class<*>, hooker: (BridgeApi.BeforeMethodInvokedCallback<Method>) -> Any?) = try {
    val clazz = classLoader.loadClass(this)
    clazz.replaceMethod(methodName, *args, hooker = hooker)
} catch (e: Throwable) {
    UniversalBridge.get().log("Error when hooking method", e)
    null
}

fun BridgeApi.HookerCallback<Method>.invokeOriginal(): Any? = when (this) {
    is BridgeApi.BeforeMethodInvokedCallback -> {
        UniversalBridge.get().invokeOriginal(getExecutable(), getThisObject(), getArgs())
    }
    is BridgeApi.AfterMethodInvokedCallback -> {
        UniversalBridge.get().invokeOriginal(getExecutable(), getThisObject(), getArgs())
    }
    else -> throw IllegalArgumentException("???")
}

fun <T: Any> BridgeApi.HookerCallback<Constructor<*>>.invokeOriginal() {
    UniversalBridge.get().invokeOriginal(getExecutable() as Constructor<T>, getThisObject() as T, getArgs())
}
