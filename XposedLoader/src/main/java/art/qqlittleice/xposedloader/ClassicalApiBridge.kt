package art.qqlittleice.xposedloader

import art.qqlittleice.xposedcompat.transition.BridgeApi
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import java.lang.reflect.Constructor
import java.lang.reflect.Member
import java.lang.reflect.Method

object ClassicalApiBridge: BridgeApi {

    override fun hook(method: Method, hooker: BridgeApi.Hooker<Method>): BridgeApi.Unhooker<Method> {
        return hook(method, BridgeApi.PRIORITY_DEFAULT, hooker)
    }

    override fun hook(
        method: Method,
        priority: Int,
        hooker: BridgeApi.Hooker<Method>
    ): BridgeApi.Unhooker<Method> {
        val unhook = XposedBridge.hookMethod(method, object : XC_MethodHook(priority) {
            override fun beforeHookedMethod(param: MethodHookParam) {
                hooker.beforeMethodInvoked(object : BridgeApi.BeforeMethodInvokedCallback<Method> {
                    override fun getExecutable(): Method = param.method as Method

                    override fun getThisObject(): Any? = param.thisObject

                    override fun getArgs(): Array<Any>? = param.args

                    override fun getResult(): Any? = param.result

                    override fun getThrowable(): Throwable? = param.throwable

                    override fun setResult(result: Any?) = param.setResult(result)

                    override fun setThrowable(throwable: Throwable?) = param.setThrowable(throwable)
                })
            }

            override fun afterHookedMethod(param: MethodHookParam) {
                hooker.afterMethodInvoked(object : BridgeApi.AfterMethodInvokedCallback<Method> {
                    override fun getExecutable(): Method = param.method as Method

                    override fun getThisObject(): Any? = param.thisObject

                    override fun getArgs(): Array<Any>? = param.args

                    override fun getResult(): Any? = param.result

                    override fun getThrowable(): Throwable? = param.throwable

                    override fun setResult(result: Any?) = param.setResult(result)

                    override fun setThrowable(throwable: Throwable?) = param.setThrowable(throwable)
                })
            }
        })
        return object : BridgeApi.Unhooker<Method> {
            override fun getExecutable(): Method = method

            override fun unhook() {
                unhook.unhook()
            }
        }
    }

    override fun hook(constructor: Constructor<*>, hooker: BridgeApi.Hooker<Constructor<*>>): BridgeApi.Unhooker<Constructor<*>> {
        return hook(constructor, BridgeApi.PRIORITY_DEFAULT, hooker)
    }

    override fun hook(
        constructor: Constructor<*>,
        priority: Int,
        hooker: BridgeApi.Hooker<Constructor<*>>
    ): BridgeApi.Unhooker<Constructor<*>> {
        val unhook = XposedBridge.hookMethod(constructor, object : XC_MethodHook(priority) {
            override fun beforeHookedMethod(param: MethodHookParam) {
                hooker.beforeMethodInvoked(object : BridgeApi.BeforeMethodInvokedCallback<Constructor<*>> {
                    override fun getExecutable(): Constructor<*> = param.method as Constructor<*>

                    override fun getThisObject(): Any? = param.thisObject

                    override fun getArgs(): Array<Any>? = param.args

                    override fun getResult(): Any? = param.result

                    override fun getThrowable(): Throwable? = param.throwable

                    override fun setResult(result: Any?) = param.setResult(result)

                    override fun setThrowable(throwable: Throwable?) = param.setThrowable(throwable)
                })
            }

            override fun afterHookedMethod(param: MethodHookParam) {
                hooker.afterMethodInvoked(object : BridgeApi.AfterMethodInvokedCallback<Constructor<*>> {
                    override fun getExecutable(): Constructor<*> = param.method as Constructor<*>

                    override fun getThisObject(): Any? = param.thisObject

                    override fun getArgs(): Array<Any>? = param.args

                    override fun getResult(): Any? = param.result

                    override fun getThrowable(): Throwable? = param.throwable

                    override fun setResult(result: Any?) = param.setResult(result)

                    override fun setThrowable(throwable: Throwable?) = param.setThrowable(throwable)
                })
            }
        })
        return object : BridgeApi.Unhooker<Constructor<*>> {
            override fun getExecutable(): Constructor<*> = constructor

            override fun unhook() {
                unhook.unhook()
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
        val unhookers = mutableListOf<BridgeApi.Unhooker<Method>>()
        val methods = clazz.declaredMethods.filter { it.name == methodName }
        for (method in methods) {
            unhookers.add(hook(method, priority, hooker))
        }
        return unhookers
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
        val unhookers = mutableListOf<BridgeApi.Unhooker<Constructor<*>>>()
        val constructors = clazz.declaredConstructors
        for (constructor in constructors) {
            unhookers.add(hook(constructor, priority, hooker))
        }
        return unhookers
    }

    override fun invokeOriginal(method: Method, thisObject: Any?, vararg args: Any?): Any? {
        return XposedBridge.invokeOriginalMethod(method, thisObject, args)
    }

    override fun <T: Any> invokeOriginal(constructor: Constructor<T>, thisObject: T, vararg args: Any?) {
        XposedBridge.invokeOriginalMethod(constructor, thisObject, args)
    }

    override fun log(msg: String) {
        XposedBridge.log(msg)
    }

    override fun log(msg: String, throwable: Throwable) {
        XposedBridge.log(msg)
        XposedBridge.log(throwable)
    }
}