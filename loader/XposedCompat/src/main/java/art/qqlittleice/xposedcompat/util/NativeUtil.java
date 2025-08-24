package art.qqlittleice.xposedcompat.util;

import java.lang.reflect.Method;

public class NativeUtil {

    static {
        System.loadLibrary("xposed-util");
    }

    public int TYPE_VOID = 0;
    public int TYPE_JOBJECT = 1;
    public int TYPE_BOOLEAN = 2;
    public int TYPE_BYTE = 3;
    public int TYPE_CHAR = 4;
    public int TYPE_SHORT = 5;
    public int TYPE_INT = 6;
    public int TYPE_LONG = 7;
    public int TYPE_FLOAT = 8;
    public int TYPE_DOUBLE = 9;

    public static native Object invokeSpecial(Class<?> clz, Method method, int callType, Object obj, Object... args);

}
