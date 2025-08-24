package art.qqlittleice.xposedcompat.unsafe

import android.annotation.SuppressLint
import sun.misc.Unsafe


@SuppressLint("DiscouragedPrivateApi")
object UnsafeUtils {

    val unsafe by lazy {
        try {
            val field = Unsafe::class.java.getDeclaredField("theUnsafe")
            field.isAccessible = true
            field.get(null) as Unsafe
        } catch (e: Exception) {
            throw RuntimeException("Can't get instance of sun.misc.Unsafe", e)
        }
    }

    fun <T> allocateInstance(clazz: Class<T>) : T {
        return unsafe.allocateInstance(clazz) as T
    }

    fun setSuper(base: Class<*>, superClass: Class<*>) {
        val superClassOffset = unsafe.objectFieldOffset(Helper.Class::class.java.getDeclaredField("superClass"))
        unsafe.putObject(base, superClassOffset, superClass)
    }

}