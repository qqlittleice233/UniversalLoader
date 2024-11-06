package art.qqlittleice.xposedcompat.transition

import android.content.pm.ApplicationInfo

class LoadedPackageParam(
    val packageName: String,
    val processName: String,
    val classLoader: ClassLoader,
    val isFirstPackage: Boolean,
    val applicationInfo: ApplicationInfo
)