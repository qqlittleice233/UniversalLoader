package art.qqlittleice.xposedcompat.transition.lsposed;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import io.github.libxposed.api.XposedInterface;

public class LSPosedHookerBeforeCallbackProxy implements InvocationHandler {

    private final XposedInterface.BeforeHookCallback callback;

    public LSPosedHookerBeforeCallbackProxy(XposedInterface.BeforeHookCallback cb) {
        this.callback = cb;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        switch (method.getName()) {
            case "getExecutable":
                return callback.getMember();
            case "getThisObject":
                return callback.getThisObject();
            case "getArgs":
                return callback.getArgs();
            case "getResult":
            case "getThrowable":
                return null;
            case "setResult":
                callback.returnAndSkip(args[0]);
                return null;
            case "setThrowable":
                callback.throwAndSkip((Throwable) args[0]);
                return null;
            default:
                throw new IllegalStateException("Unknown method: " + method.getName());
        }
    }
}
