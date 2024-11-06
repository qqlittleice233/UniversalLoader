package art.qqlittleice.xposedloader

import art.qqlittleice.xposedcompat.transition.LoadedPackageParam
import art.qqlittleice.xposedcompat.transition.ModulePackageParam
import art.qqlittleice.xposedcompat.transition.SystemServerLoadedParam
import art.qqlittleice.xposedcompat.transition.BridgeApi
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface
import io.github.libxposed.api.XposedModuleInterface.ModuleLoadedParam

class ModernApiLoader(base: XposedInterface, param: ModuleLoadedParam): XposedModule(base, param), ApiLoader {

    private var sParam: XposedModuleInterface.SystemServerLoadedParam? = null
    private var lParam: XposedModuleInterface.PackageLoadedParam? = null

    override fun onSystemServerLoaded(param: XposedModuleInterface.SystemServerLoadedParam) {
        sParam = param
        Loader.loadSystemEntry(this)
    }

    override fun onPackageLoaded(param: XposedModuleInterface.PackageLoadedParam) {
        lParam = param
        Loader.loadPackageEntry(this)
    }

    override fun getBridge(): BridgeApi = ModernApiBridge(this)

    override fun createModulePackageParam(): ModulePackageParam {
        return ModulePackageParam(applicationInfo.sourceDir)
    }

    override fun createLoadedPackageParam(): LoadedPackageParam {
        return LoadedPackageParam(
            lParam!!.packageName,
            lParam!!.applicationInfo.processName,
            lParam!!.classLoader,
            lParam!!.isFirstPackage,
            lParam!!.applicationInfo
        )
    }

    override fun createSystemServerLoadedParam(): SystemServerLoadedParam {
        return SystemServerLoadedParam(ClassLoader.getSystemClassLoader())
    }
}