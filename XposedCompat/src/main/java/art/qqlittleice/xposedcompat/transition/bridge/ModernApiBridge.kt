package art.qqlittleice.xposedcompat.transition.bridge

import android.content.SharedPreferences
import android.os.ParcelFileDescriptor
import art.qqlittleice.xposedcompat.transition.lsposed.LSPosedHookerFactory
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.utils.DexParser
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.nio.ByteBuffer

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

    fun <T> hookClassInitializer(origin: Class<T>, hooker: BridgeApi.Hooker<Constructor<T>>): BridgeApi.Unhooker<Constructor<T>> {
        return hookClassInitializer(origin, BridgeApi.PRIORITY_DEFAULT, hooker)
    }

    fun <T> hookClassInitializer(origin: Class<T>, priority: Int, hooker: BridgeApi.Hooker<Constructor<T>>): BridgeApi.Unhooker<Constructor<T>> {
        val proxy = LSPosedHookerFactory.createHooker(hooker)
        val unhook = lsposedBridge.hookClassInitializer(origin, priority, proxy)
        return object : BridgeApi.Unhooker<Constructor<T>> {
            override fun getExecutable(): Constructor<T> {
                return unhook.origin
            }
            override fun unhook() {
                unhook.unhook()
                LSPosedHookerFactory.requiredDestroy(proxy)
            }

        }
    }

    override fun log(msg: String) {
        lsposedBridge.log(msg)
    }

    override fun log(msg: String, throwable: Throwable) {
        lsposedBridge.log(msg, throwable)
    }

    fun getRemotePreferences(group: String): SharedPreferences = lsposedBridge.getRemotePreferences(group)

    fun openRemoteFile(name: String): ParcelFileDescriptor = lsposedBridge.openRemoteFile(name)

    fun listRemoteFiles(): Array<String> = lsposedBridge.listRemoteFiles()

    fun parseDex(byteData: ByteBuffer, includeAnnotations: Boolean): DexParser? = lsposedBridge.parseDex(byteData, includeAnnotations)

    fun deoptimize(method: Method): Boolean = lsposedBridge.deoptimize(method)

    fun <T> deoptimize(constructor: Constructor<T>): Boolean = lsposedBridge.deoptimize(constructor)

}