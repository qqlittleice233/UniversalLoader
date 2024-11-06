package art.qqlittleice.xposedloader

import android.util.Log
import art.qqlittleice.xposedcompat.transition.LoadedPackageParam
import art.qqlittleice.xposedcompat.transition.ModulePackageParam
import art.qqlittleice.xposedcompat.transition.SystemServerLoadedParam
import art.qqlittleice.xposedcompat.transition.UniversalBridge
import art.qqlittleice.xposedcompat.transition.BridgeApi

object Loader {

    private const val TAG = "UniversalLoader"

    fun loadSystemEntry(loader: ApiLoader) {
        initBridge(loader.getBridge())
        runCatching {
            val systemServerLoadedParam = loader.createSystemServerLoadedParam()
            Class.forName("art.qqlittleice.xposedloader.UniversalLoader")
                .getDeclaredMethod("systemServerLoaded", SystemServerLoadedParam::class.java)
                .invoke(null, systemServerLoadedParam)
        }.onFailure { Log.d(TAG, "Error to invoke Universal Loader", it) }
    }

    fun loadPackageEntry(loader: ApiLoader) {
        initBridge(loader.getBridge())
        runCatching {
            val modulePackageParam = loader.createModulePackageParam()
            val loadedPackageParam = loader.createLoadedPackageParam()
            Class.forName("art.qqlittleice.xposedloader.UniversalLoader")
                .getDeclaredMethod("packageLoaded", ModulePackageParam::class.java, LoadedPackageParam::class.java)
                .invoke(null, modulePackageParam, loadedPackageParam)
        }.onFailure { Log.d(TAG, "Error to invoke Universal Loader", it) }
    }

    private fun initBridge(bridge: BridgeApi) {
        runCatching {
            if (UniversalBridge.INSTANCE != null) return
            val constructor = UniversalBridge::class.java.getDeclaredConstructor(BridgeApi::class.java)
            constructor.isAccessible = true
            UniversalBridge.INSTANCE = constructor.newInstance(bridge)
        }.onFailure { Log.d(TAG, "Error to init bridge", it) }
    }

}