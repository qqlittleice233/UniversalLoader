package art.qqlittleice.xposedcompat.transition

import art.qqlittleice.xposedcompat.transition.bridge.BridgeApi

interface ApiLoader: ModulePackageParamFactory, LoadedPackageParamFactory, SystemServerLoadedParamFactory {

    fun getBridge(): BridgeApi

}