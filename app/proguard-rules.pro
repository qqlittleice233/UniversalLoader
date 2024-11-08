# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class art.qqlittleice.xposedloader.UniversalLoader { *; }
-keep class * extends art.qqlittleice.xposedloader.UniversalLoader { *; }

-dontwarn de.robv.android.xposed.IXposedHookLoadPackage
-dontwarn de.robv.android.xposed.IXposedHookZygoteInit$StartupParam
-dontwarn de.robv.android.xposed.IXposedHookZygoteInit
-dontwarn de.robv.android.xposed.XC_MethodHook$MethodHookParam
-dontwarn de.robv.android.xposed.XC_MethodHook$Unhook
-dontwarn de.robv.android.xposed.XC_MethodHook
-dontwarn de.robv.android.xposed.XposedBridge
-dontwarn de.robv.android.xposed.callbacks.XC_LoadPackage$LoadPackageParam
-dontwarn io.github.libxposed.api.XposedInterface$AfterHookCallback
-dontwarn io.github.libxposed.api.XposedInterface$BeforeHookCallback
-dontwarn io.github.libxposed.api.XposedInterface$Hooker
-dontwarn io.github.libxposed.api.XposedInterface$MethodUnhooker
-dontwarn io.github.libxposed.api.XposedInterface
-dontwarn io.github.libxposed.api.XposedModule
-dontwarn io.github.libxposed.api.XposedModuleInterface$ModuleLoadedParam
-dontwarn io.github.libxposed.api.XposedModuleInterface$PackageLoadedParam
-dontwarn io.github.libxposed.api.XposedModuleInterface$SystemServerLoadedParam
-dontwarn io.github.libxposed.api.utils.DexParser