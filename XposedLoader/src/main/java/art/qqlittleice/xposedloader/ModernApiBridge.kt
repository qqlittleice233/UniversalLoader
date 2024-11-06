package art.qqlittleice.xposedloader

import art.qqlittleice.xposedcompat.transition.BridgeApi
import art.qqlittleice.xposedcompat.transition.lsposed.LSPosedHookerFactory
import io.github.libxposed.api.XposedInterface
import java.lang.reflect.Constructor
import java.lang.reflect.Method

class ModernApiBridge(private val lsposedBridge: XposedInterface): BridgeApi {
    override fun hook(method: Method, hooker: BridgeApi.Hooker<Method>): BridgeApi.Unhooker<Method> {
        return hook(method, BridgeApi.PRIORITY_DEFAULT, hooker)
    }

    override fun hook(
        method: Method,
        priority: Int,
        hooker: BridgeApi.Hooker<Method>
    ): BridgeApi.Unhooker<Method> {
        val proxy = LSPosedHookerFactory.createHooker(hooker)
        val unhook = lsposedBridge.hook(method, priority, proxy)
        return object : BridgeApi.Unhooker<Method> {
            override fun getExecutable(): Method {
                return unhook.origin
            }
            override fun unhook() {
                unhook.unhook()
                LSPosedHookerFactory.requiredDestroy(proxy)
            }
        }
    }

    override fun hook(
        constructor: Constructor<*>,
        hooker: BridgeApi.Hooker<Constructor<*>>
    ): BridgeApi.Unhooker<Constructor<*>> {
        return hook(constructor, BridgeApi.PRIORITY_DEFAULT, hooker)
    }

    override fun hook(
        constructor: Constructor<*>,
        priority: Int,
        hooker: BridgeApi.Hooker<Constructor<*>>
    ): BridgeApi.Unhooker<Constructor<*>> {
        val proxy = LSPosedHookerFactory.createHooker(hooker)
        val unhook = lsposedBridge.hook(constructor, priority, proxy)
        return object : BridgeApi.Unhooker<Constructor<*>> {
            override fun getExecutable(): Constructor<*> {
                return unhook.origin
            }
            override fun unhook() {
                unhook.unhook()
                LSPosedHookerFactory.requiredDestroy(proxy)
            }
        }
    }

    override fun hookAllMethods(
        clazz: Class<*>,
        methodName: String,
        hooker: BridgeApi.Hooker<Method>
    ): List<BridgeApi.Unhooker<Method>> {
        return hookAllMethods(clazz, methodName, BridgeApi.PRIORITY_DEFAULT, hooker)
    }

    override fun hookAllMethods(
        clazz: Class<*>,
        methodName: String,
        priority: Int,
        hooker: BridgeApi.Hooker<Method>
    ): List<BridgeApi.Unhooker<Method>> {
        val proxy = LSPosedHookerFactory.createHooker(hooker)
        return clazz.declaredMethods
            .filter { it.name == methodName }
            .map { method ->
                val unhook = lsposedBridge.hook(method, priority, proxy)
                object : BridgeApi.Unhooker<Method> {
                    override fun getExecutable(): Method {
                        return unhook.origin
                    }
                    override fun unhook() {
                        unhook.unhook()
                        LSPosedHookerFactory.requiredDestroy(proxy)
                    }

                }
            }
    }

    override fun hookAllConstructors(
        clazz: Class<*>,
        hooker: BridgeApi.Hooker<Constructor<*>>
    ): List<BridgeApi.Unhooker<Constructor<*>>> {
        return hookAllConstructors(clazz, BridgeApi.PRIORITY_DEFAULT, hooker)
    }

    override fun hookAllConstructors(
        clazz: Class<*>,
        priority: Int,
        hooker: BridgeApi.Hooker<Constructor<*>>
    ): List<BridgeApi.Unhooker<Constructor<*>>> {
        val proxy = LSPosedHookerFactory.createHooker(hooker)
        return clazz.declaredConstructors
            .map { constructor ->
                val unhook = lsposedBridge.hook(constructor, priority, proxy)
                object : BridgeApi.Unhooker<Constructor<*>> {
                    override fun getExecutable(): Constructor<*> {
                        return unhook.origin
                    }
                    override fun unhook() {
                        unhook.unhook()
                        LSPosedHookerFactory.requiredDestroy(proxy)
                    }
                }
            }
    }

    override fun invokeOriginal(method: Method, thisObject: Any?, vararg args: Any?): Any? {
        return lsposedBridge.invokeOrigin(method, thisObject, args)
    }

    override fun <T : Any> invokeOriginal(constructor: Constructor<T>, thisObject: T, vararg args: Any?) {
        lsposedBridge.invokeOrigin(constructor, thisObject, args)
    }

    override fun log(msg: String) {
        lsposedBridge.log(msg)
    }

    override fun log(msg: String, throwable: Throwable) {
        lsposedBridge.log(msg, throwable)
    }
}