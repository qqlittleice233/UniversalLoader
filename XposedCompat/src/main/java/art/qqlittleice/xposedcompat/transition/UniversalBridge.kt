package art.qqlittleice.xposedcompat.transition

import java.lang.reflect.Constructor
import java.lang.reflect.Method

class UniversalBridge private constructor(private val mBridge: BridgeApi) : BridgeApi {

    companion object {

        @JvmStatic
        var INSTANCE: UniversalBridge? = null

        @JvmStatic
        fun get(): UniversalBridge {
            return INSTANCE ?: throw IllegalStateException("UniversalBridge is not initialized")
        }
    }

    override fun hook(method: Method, hooker: BridgeApi.Hooker): BridgeApi.Unhooker<Method> {
        return mBridge.hook(method, hooker)
    }

    override fun hook(
        method: Method,
        priority: Int,
        hooker: BridgeApi.Hooker
    ): BridgeApi.Unhooker<Method> {
        return mBridge.hook(method, priority, hooker)
    }

    override fun hook(
        constructor: Constructor<*>,
        hooker: BridgeApi.Hooker
    ): BridgeApi.Unhooker<Constructor<*>> {
        return mBridge.hook(constructor, hooker)
    }

    override fun hook(
        constructor: Constructor<*>,
        priority: Int,
        hooker: BridgeApi.Hooker
    ): BridgeApi.Unhooker<Constructor<*>> {
        return mBridge.hook(constructor, priority, hooker)
    }

    override fun hookAllMethods(
        clazz: Class<*>,
        methodName: String,
        hooker: BridgeApi.Hooker
    ): List<BridgeApi.Unhooker<Method>> {
        return mBridge.hookAllMethods(clazz, methodName, hooker)
    }

    override fun hookAllMethods(
        clazz: Class<*>,
        methodName: String,
        priority: Int,
        hooker: BridgeApi.Hooker
    ): List<BridgeApi.Unhooker<Method>> {
        return mBridge.hookAllMethods(clazz, methodName, priority, hooker)
    }

    override fun hookAllConstructors(
        clazz: Class<*>,
        hooker: BridgeApi.Hooker
    ): List<BridgeApi.Unhooker<Constructor<*>>> {
        return mBridge.hookAllConstructors(clazz, hooker)
    }

    override fun hookAllConstructors(
        clazz: Class<*>,
        priority: Int,
        hooker: BridgeApi.Hooker
    ): List<BridgeApi.Unhooker<Constructor<*>>> {
        return mBridge.hookAllConstructors(clazz, priority, hooker)
    }

    override fun invokeOriginal(method: Method, thisObject: Any?, vararg args: Any?): Any? {
        return mBridge.invokeOriginal(method, thisObject, args)
    }

    override fun <T: Any> invokeOriginal(constructor: Constructor<T>, thisObject: T, vararg args: Any?) {
        return mBridge.invokeOriginal(constructor, thisObject, args)
    }

    override fun log(msg: String) {
        mBridge.log(msg)
    }

    override fun log(msg: String, throwable: Throwable) {
        mBridge.log(msg, throwable)
    }

}