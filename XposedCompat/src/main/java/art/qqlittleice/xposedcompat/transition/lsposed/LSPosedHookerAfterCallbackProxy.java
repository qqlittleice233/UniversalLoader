package art.qqlittleice.xposedcompat.transition.lsposed;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import io.github.libxposed.api.XposedInterface;

public class LSPosedHookerAfterCallbackProxy implements InvocationHandler {

    private final XposedInterface.AfterHookCallback callback;

    public LSPosedHookerAfterCallbackProxy(XposedInterface.AfterHookCallback cb) {
        this.callback = cb;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        switch (method.getName()) {
            case "getMember":
                return callback.getMember();
            case "getThisObject":
                return callback.getThisObject();
            case "getArgs":
                return callback.getArgs();
            case "getResult":
                return callback.getResult();
            case "getThrowable":
                return callback.getThrowable();
            case "setResult":
                callback.setResult(args[0]);
                return null;
            case "setThrowable":
                callback.setThrowable((Throwable) args[0]);
                return null;
            default:
                throw new IllegalStateException("Unknown method: " + method.getName());
        }
    }
}
