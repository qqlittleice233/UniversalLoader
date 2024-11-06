package libcore.io;

public final class Memory {

    private Memory() {
    }

    public static native void peekByteArray(long address, byte[] dst, int dstOffset, int byteCount);

    public static native void pokeByteArray(long address, byte[] src, int offset, int count);

    // There is no peek/poke for short because it's blocked by the hidden API access restriction.
    // I have no idea why it's blocked, but it's blocked.

    public static native byte peekByte(long address);

    public static int peekInt(long address, boolean swap) {
        int result = peekIntNative(address);
        if (swap) {
            result = Integer.reverseBytes(result);
        }
        return result;
    }

    private static native int peekIntNative(long address);

    public static long peekLong(long address, boolean swap) {
        long result = peekLongNative(address);
        if (swap) {
            result = Long.reverseBytes(result);
        }
        return result;
    }

    private static native long peekLongNative(long address);

    public static native void pokeByte(long address, byte value);

    public static void pokeInt(long address, int value, boolean swap) {
        if (swap) {
            value = Integer.reverseBytes(value);
        }
        pokeIntNative(address, value);
    }

    private static native void pokeIntNative(long address, int value);

    public static void pokeLong(long address, long value, boolean swap) {
        if (swap) {
            value = Long.reverseBytes(value);
        }
        pokeLongNative(address, value);
    }

    private static native void pokeLongNative(long address, long value);

}
