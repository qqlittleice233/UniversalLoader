package com.example.universalloader

import art.qqlittleice.xposedcompat.transition.LoadedPackageParam
import art.qqlittleice.xposedcompat.transition.ModulePackageParam
import art.qqlittleice.xposedcompat.transition.SystemServerLoadedParam
import art.qqlittleice.xposedcompat.transition.bridge.UniversalBridge
import art.qqlittleice.xposedloader.UniversalLoader


class XposedEntry(bridge: UniversalBridge) : UniversalLoader(bridge) {

    override fun onPackageLoaded(
        modulePackageParam: ModulePackageParam,
        loadedPackageParam: LoadedPackageParam
    ) {
        bridge.log("module path: ${modulePackageParam.modulePath}")
        bridge.log("onPackageLoaded: ${loadedPackageParam.packageName} - ${loadedPackageParam.processName}")
        
    }

    override fun onSystemServerLoaded(systemServerLoadedParam: SystemServerLoadedParam) {
        bridge.log("onSystemServerLoaded: ${systemServerLoadedParam.systemClassLoader}")
    }

}
