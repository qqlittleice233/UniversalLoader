/*
 * Copyright (c) 2000, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package sun.misc;

import android.compat.annotation.UnsupportedAppUsage;

import dalvik.annotation.compat.VersionCodes;

@SuppressWarnings({"unchecked", "deprecation", "all"})
public final class Unsafe {

    private Unsafe() {
        throw new RuntimeException("Stub!");
    }

    @UnsupportedAppUsage
    public static Unsafe getUnsafe() {
        throw new RuntimeException("Stub!");
    }

    @UnsupportedAppUsage
    public long objectFieldOffset(java.lang.reflect.Field field) {
        throw new RuntimeException("Stub!");
    }

    @UnsupportedAppUsage
    public int arrayBaseOffset(Class clazz) {
        throw new RuntimeException("Stub!");
    }

    @UnsupportedAppUsage
    public int arrayIndexScale(Class clazz) {
        throw new RuntimeException("Stub!");
    }

    @UnsupportedAppUsage(maxTargetSdk = VersionCodes.O)
    private static native int getArrayBaseOffsetForComponentType(Class component_class);

    @UnsupportedAppUsage(maxTargetSdk = VersionCodes.O)
    private static native int getArrayIndexScaleForComponentType(Class component_class);

    @UnsupportedAppUsage
    public native boolean compareAndSwapInt(
            Object obj, long offset, int expectedValue, int newValue);

    @UnsupportedAppUsage
    public native boolean compareAndSwapLong(
            Object obj, long offset, long expectedValue, long newValue);

    @UnsupportedAppUsage
    public native boolean compareAndSwapObject(
            Object obj,
            long offset,
            Object expectedValue,
            Object newValue);

    @UnsupportedAppUsage
    public native int getIntVolatile(Object obj, long offset);

    @UnsupportedAppUsage
    public native void putIntVolatile(Object obj, long offset, int newValue);

    @UnsupportedAppUsage
    public native long getLongVolatile(Object obj, long offset);

    @UnsupportedAppUsage
    public native void putLongVolatile(Object obj, long offset, long newValue);

    @UnsupportedAppUsage
    public native Object getObjectVolatile(Object obj, long offset);

    @UnsupportedAppUsage
    public native void putObjectVolatile(
            Object obj, long offset, Object newValue);

    @UnsupportedAppUsage
    public native int getInt(Object obj, long offset);

    @UnsupportedAppUsage
    public native void putInt(Object obj, long offset, int newValue);

    @UnsupportedAppUsage
    public native void putOrderedInt(Object obj, long offset, int newValue);

    @UnsupportedAppUsage
    public native long getLong(Object obj, long offset);

    @UnsupportedAppUsage
    public native void putLong(Object obj, long offset, long newValue);

    @UnsupportedAppUsage
    public native void putOrderedLong(Object obj, long offset, long newValue);

    @UnsupportedAppUsage
    public native Object getObject(Object obj, long offset);

    @UnsupportedAppUsage
    public native void putObject(Object obj, long offset, Object newValue);

    @UnsupportedAppUsage
    public native void putOrderedObject(
            Object obj, long offset, Object newValue);

    @UnsupportedAppUsage
    public native boolean getBoolean(Object obj, long offset);

    @UnsupportedAppUsage
    public native void putBoolean(Object obj, long offset, boolean newValue);

    @UnsupportedAppUsage
    public native byte getByte(Object obj, long offset);

    @UnsupportedAppUsage
    public native void putByte(Object obj, long offset, byte newValue);

    @UnsupportedAppUsage
    public native char getChar(Object obj, long offset);

    @UnsupportedAppUsage
    public native void putChar(Object obj, long offset, char newValue);

    @UnsupportedAppUsage
    public native short getShort(Object obj, long offset);

    @UnsupportedAppUsage
    public native void putShort(Object obj, long offset, short newValue);

    @UnsupportedAppUsage
    public native float getFloat(Object obj, long offset);

    @UnsupportedAppUsage
    public native void putFloat(Object obj, long offset, float newValue);

    @UnsupportedAppUsage
    public native double getDouble(Object obj, long offset);

    @UnsupportedAppUsage
    public native void putDouble(Object obj, long offset, double newValue);

    @UnsupportedAppUsage
    public void park(boolean absolute, long time) {
        throw new RuntimeException("Stub!");
    }

    @UnsupportedAppUsage
    public void unpark(Object obj) {
        throw new RuntimeException("Stub!");
    }

    @UnsupportedAppUsage
    public native Object allocateInstance(Class<?> c);

    @UnsupportedAppUsage
    public native int addressSize();

    @UnsupportedAppUsage
    public native int pageSize();

    @UnsupportedAppUsage
    public native long allocateMemory(long bytes);

    @UnsupportedAppUsage
    public native void freeMemory(long address);

    @UnsupportedAppUsage
    public native void setMemory(long address, long bytes, byte value);

    @UnsupportedAppUsage
    public native byte getByte(long address);

    @UnsupportedAppUsage
    public native void putByte(long address, byte x);

    @UnsupportedAppUsage
    public native short getShort(long address);

    @UnsupportedAppUsage
    public native void putShort(long address, short x);

    @UnsupportedAppUsage
    public native char getChar(long address);

    @UnsupportedAppUsage
    public native void putChar(long address, char x);

    @UnsupportedAppUsage
    public native int getInt(long address);

    @UnsupportedAppUsage
    public native void putInt(long address, int x);

    @UnsupportedAppUsage
    public native long getLong(long address);

    @UnsupportedAppUsage
    public native void putLong(long address, long x);

    @UnsupportedAppUsage
    public native float getFloat(long address);

    @UnsupportedAppUsage
    public native void putFloat(long address, float x);

    @UnsupportedAppUsage
    public native double getDouble(long address);

    @UnsupportedAppUsage
    public native void putDouble(long address, double x);

    @UnsupportedAppUsage(maxTargetSdk = 30, trackingBug = 170729553)
    public native void copyMemoryToPrimitiveArray(
            long srcAddr, Object dst, long dstOffset, long bytes);

    @UnsupportedAppUsage(maxTargetSdk = 30, trackingBug = 170729553)
    public native void copyMemoryFromPrimitiveArray(
            Object src, long srcOffset, long dstAddr, long bytes);

    @UnsupportedAppUsage
    public native void copyMemory(long srcAddr, long dstAddr, long bytes);

    @UnsupportedAppUsage(maxTargetSdk = 30, trackingBug = 170729553)
    public int getAndAddInt(Object o, long offset, int delta) {
        throw new RuntimeException("Stub!");
    }

    @UnsupportedAppUsage(maxTargetSdk = 30, trackingBug = 170729553)
    public long getAndAddLong(Object o, long offset, long delta) {
        throw new RuntimeException("Stub!");
    }

    @UnsupportedAppUsage(maxTargetSdk = 30, trackingBug = 170729553)
    public int getAndSetInt(Object o, long offset, int newValue) {
        throw new RuntimeException("Stub!");
    }

    @UnsupportedAppUsage(maxTargetSdk = 30, trackingBug = 170729553)
    public long getAndSetLong(Object o, long offset, long newValue) {
        throw new RuntimeException("Stub!");
    }

    @UnsupportedAppUsage
    public Object getAndSetObject(
            Object o, long offset, Object newValue) {
        throw new RuntimeException("Stub!");
    }

    @UnsupportedAppUsage
    public native void loadFence();

    @UnsupportedAppUsage
    public native void storeFence();

    @UnsupportedAppUsage
    public native void fullFence();

    @UnsupportedAppUsage(maxTargetSdk = 30, trackingBug = 170729553)
    public static final int INVALID_FIELD_OFFSET = -1; // 0xffffffff

    @UnsupportedAppUsage private static final Unsafe THE_ONE;

    static {
        THE_ONE = null;
    }

    @UnsupportedAppUsage private static final Unsafe theUnsafe;

    static {
        theUnsafe = null;
    }
}