package art.qqlittleice.xposedloader

import art.qqlittleice.xposedcompat.transition.BridgeApi
import art.qqlittleice.xposedcompat.transition.LoadedPackageParamFactory
import art.qqlittleice.xposedcompat.transition.ModulePackageParamFactory
import art.qqlittleice.xposedcompat.transition.SystemServerLoadedParamFactory

interface ApiLoader: ModulePackageParamFactory, LoadedPackageParamFactory, SystemServerLoadedParamFactory {

    fun getBridge(): BridgeApi

}