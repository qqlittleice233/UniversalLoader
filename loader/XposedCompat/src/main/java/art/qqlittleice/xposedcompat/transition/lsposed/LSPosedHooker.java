package art.qqlittleice.xposedcompat.transition.lsposed;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import io.github.libxposed.api.XposedInterface;

public class LSPosedHooker implements XposedInterface.Hooker {

    public static Object instance;

    public static Class<?> beforeMethodInvokedCallbackClass;
    public static Class<?> afterMethodInvokedCallbackClass;
    public static ClassLoader moduleClassLoader;

    public static Method beforeMethodInvoked;
    public static Method afterMethodInvoked;

    public static void initMethod() throws NoSuchMethodException {
        beforeMethodInvoked = instance.getClass().getDeclaredMethod("beforeMethodInvoked", beforeMethodInvokedCallbackClass);
        afterMethodInvoked = instance.getClass().getDeclaredMethod("afterMethodInvoked", afterMethodInvokedCallbackClass);
    }

    public static Object createBeforeCallbackProxy(XposedInterface.BeforeHookCallback callback) {
        return Proxy.newProxyInstance(moduleClassLoader, new Class[]{beforeMethodInvokedCallbackClass}, new LSPosedHookerBeforeCallbackProxy(callback));
    }

    public static Object createAfterCallbackProxy(XposedInterface.AfterHookCallback callback) {
        return Proxy.newProxyInstance(moduleClassLoader, new Class[]{afterMethodInvokedCallbackClass}, new LSPosedHookerAfterCallbackProxy(callback));
    }

    public static void before(XposedInterface.BeforeHookCallback callback) throws InvocationTargetException, IllegalAccessException {
        beforeMethodInvoked.invoke(instance, createBeforeCallbackProxy(callback));
    }

    public static void after(XposedInterface.AfterHookCallback callback) throws InvocationTargetException, IllegalAccessException {
        afterMethodInvoked.invoke(instance, createAfterCallbackProxy(callback));
    }

}
