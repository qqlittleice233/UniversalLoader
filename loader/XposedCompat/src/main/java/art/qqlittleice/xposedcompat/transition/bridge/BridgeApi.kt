package art.qqlittleice.xposedcompat.transition.bridge

import java.lang.reflect.Constructor
import java.lang.reflect.Executable
import java.lang.reflect.Method

interface BridgeApi {

    companion object {
        /**
         * The default hook priority.
         */
        const val PRIORITY_DEFAULT: Int = 50

        /**
         * Execute the hook callback late.
         */
        const val PRIORITY_LOWEST: Int = -10000

        /**
         * Execute the hook callback early.
         */
        const val PRIORITY_HIGHEST: Int = 10000
    }

    fun hook(method: Method, hooker: Hooker<Method>): Unhooker<Method>

    fun hook(method: Method, priority: Int, hooker: Hooker<Method>): Unhooker<Method>

    fun hook(constructor: Constructor<*>, hooker: Hooker<Constructor<*>>): Unhooker<Constructor<*>>

    fun hook(constructor: Constructor<*>, priority: Int, hooker: Hooker<Constructor<*>>): Unhooker<Constructor<*>>

    fun hookAllMethods(clazz: Class<*>, methodName: String, hooker: Hooker<Method>): List<Unhooker<Method>>

    fun hookAllMethods(clazz: Class<*>, methodName: String, priority: Int, hooker: Hooker<Method>): List<Unhooker<Method>>

    fun hookAllConstructors(clazz: Class<*>, hooker: Hooker<Constructor<*>>): List<Unhooker<Constructor<*>>>

    fun hookAllConstructors(clazz: Class<*>, priority: Int, hooker: Hooker<Constructor<*>>): List<Unhooker<Constructor<*>>>

    fun invokeOriginal(method: Method, thisObject: Any?, vararg args: Any?): Any?

    fun <T : Any> invokeOriginal(constructor: Constructor<T>, thisObject: T, vararg args: Any?)

    fun log(msg: String)

    fun log(msg: String, throwable: Throwable)

    interface Hooker <T: Executable> {

        fun beforeMethodInvoked(callback: BeforeMethodInvokedCallback<T>)

        fun afterMethodInvoked(callback: AfterMethodInvokedCallback<T>)

    }

    interface BeforeMethodInvokedCallback <T: Executable>: HookerCallback<T> {

        fun returnAndSkip(result: Any?)

        fun throwAndSkip(throwable: Throwable?)

    }

    interface AfterMethodInvokedCallback <T: Executable>: HookerCallback<T> {

        fun setResult(result: Any?)

        fun setThrowable(throwable: Throwable?)

    }

    interface HookerCallback <T: Executable> {

        fun getExecutable(): T

        fun getThisObject(): Any?

        fun getArgs(): Array<Any?>

        fun getResult(): Any?

        fun getThrowable(): Throwable?

    }

    interface Unhooker<T : Executable> {

        fun getExecutable(): T

        fun unhook()

    }

}