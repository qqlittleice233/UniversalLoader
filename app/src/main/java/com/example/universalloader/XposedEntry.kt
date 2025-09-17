package com.example.universalloader

import android.os.Bundle
import android.view.View
import art.qqlittleice.xposedcompat.transition.LoadedPackageParam
import art.qqlittleice.xposedcompat.transition.ModulePackageParam
import art.qqlittleice.xposedcompat.transition.SystemServerLoadedParam
import art.qqlittleice.xposedcompat.transition.bridge.UniversalBridge
import art.qqlittleice.xposedcompat.util.ObjectPrinter
import art.qqlittleice.xposedcompat.util.hookAfterMethod
import art.qqlittleice.xposedcompat.util.hookBeforeMethod
import art.qqlittleice.xposedcompat.util.invokeSuper
import art.qqlittleice.xposedloader.UniversalLoader


class XposedEntry(bridge: UniversalBridge) : UniversalLoader(bridge) {

    override fun onPackageLoaded(
        modulePackageParam: ModulePackageParam,
        loadedPackageParam: LoadedPackageParam
    ) {
        bridge.log("module path: ${modulePackageParam.modulePath}")
        bridge.log("onPackageLoaded: ${loadedPackageParam.packageName} - ${loadedPackageParam.processName}")
        if (loadedPackageParam.packageName == "com.miui.contentcatcher") return
        //
    }

    override fun onSystemServerLoaded(systemServerLoadedParam: SystemServerLoadedParam) {
        bridge.log("onSystemServerLoaded: ${systemServerLoadedParam.systemClassLoader}")
    }

}
