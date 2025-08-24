package art.qqlittleice.xposedloader

import art.qqlittleice.xposedcompat.transition.ApiLoader
import art.qqlittleice.xposedcompat.transition.LoadedPackageParam
import art.qqlittleice.xposedcompat.transition.ModulePackageParam
import art.qqlittleice.xposedcompat.transition.SystemServerLoadedParam
import art.qqlittleice.xposedcompat.transition.bridge.BridgeApi
import art.qqlittleice.xposedcompat.transition.bridge.ClassicalApiBridge
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage

class ClassicalApiLoader : IXposedHookZygoteInit, IXposedHookLoadPackage, ApiLoader {

    private var sParam: IXposedHookZygoteInit.StartupParam? = null
    private var lParam: XC_LoadPackage.LoadPackageParam? = null

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        sParam = startupParam
        Loader.loadSystemEntry(this)
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        lParam = lpparam
        Loader.loadPackageEntry(this)
    }

    override fun getBridge(): BridgeApi = ClassicalApiBridge

    override fun createModulePackageParam(): ModulePackageParam {
        return ModulePackageParam(sParam!!.modulePath)
    }

    override fun createLoadedPackageParam(): LoadedPackageParam {
        return LoadedPackageParam(
            lParam!!.packageName,
            lParam!!.processName,
            lParam!!.classLoader,
            lParam!!.isFirstApplication,
            lParam!!.appInfo
        )
    }

    override fun createSystemServerLoadedParam(): SystemServerLoadedParam {
        return SystemServerLoadedParam(ClassLoader.getSystemClassLoader())
    }


}