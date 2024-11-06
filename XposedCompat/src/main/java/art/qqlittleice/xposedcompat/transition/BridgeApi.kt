package art.qqlittleice.xposedcompat.transition

import java.lang.reflect.Constructor
import java.lang.reflect.Member
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

    fun hook(method: Method, hooker: Hooker): Unhooker<Method>

    fun hook(method: Method, priority: Int, hooker: Hooker): Unhooker<Method>

    fun hook(constructor: Constructor<*>, hooker: Hooker): Unhooker<Constructor<*>>

    fun hook(constructor: Constructor<*>, priority: Int, hooker: Hooker): Unhooker<Constructor<*>>

    fun hookAllMethods(clazz: Class<*>, methodName: String, hooker: Hooker): List<Unhooker<Method>>

    fun hookAllMethods(clazz: Class<*>, methodName: String, priority: Int, hooker: Hooker): List<Unhooker<Method>>

    fun hookAllConstructors(clazz: Class<*>, hooker: Hooker): List<Unhooker<Constructor<*>>>

    fun hookAllConstructors(clazz: Class<*>, priority: Int, hooker: Hooker): List<Unhooker<Constructor<*>>>

    fun invokeOriginal(method: Method, thisObject: Any?, vararg args: Any?): Any?

    fun <T : Any> invokeOriginal(constructor: Constructor<T>, thisObject: T, vararg args: Any?)

    fun log(msg: String)

    fun log(msg: String, throwable: Throwable)

    interface Hooker {

        fun beforeMethodInvoked(callback: BeforeMethodInvokedCallback)

        fun afterMethodInvoked(callback: AfterMethodInvokedCallback)

    }

    interface BeforeMethodInvokedCallback: HookerCallback

    interface AfterMethodInvokedCallback: HookerCallback

    interface HookerCallback {

        fun getMember(): Member

        fun getThisObject(): Any?

        fun getArgs(): Array<Any>?

        fun getResult(): Any?

        fun getThrowable(): Throwable?

        fun setResult(result: Any?)

        fun setThrowable(throwable: Throwable?)

    }

    interface Unhooker<T : Member> {

        fun getMember(): T

        fun unhook()

    }

}