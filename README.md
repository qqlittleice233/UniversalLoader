# UniversalLoader
Building your own xposed module with both support [Xposed Api](https://github.com/rovo89/xposedbridge/wiki/using-the-xposed-framework-api) and [New Xposed Api (LSPosed)](https://github.com/LSPosed/LSPosed/wiki/Develop-Xposed-Modules-Using-Modern-Xposed-API)

## Quick Start

### 1. Write your own Xposed Module entry class with extends of UniversalLoader.class
### Java
```java
import art.qqlittleice.xposedcompat.transition.UniversalBridge;
import art.qqlittleice.xposedloader.UniversalLoader;

public class TestEntry extends UniversalLoader {
    
    public TestEntry(UniversalBridge bridge) {
        super(bridge);
    }
    
}
```
### Kotlin
```kotlin
import art.qqlittleice.xposedcompat.transition.UniversalBridge
import art.qqlittleice.xposedloader.UniversalLoader

class TestEntry(bridge: UniversalBridge): UniversalLoader(bridge) {
    
}
```

### 2. Add your own loader class to art.qqlittleice.xposedloader.UniversalLoader.registeredOnLoadedEntry
```java
public abstract class UniversalLoader {

    private static final List<Class<? extends UniversalLoader>> registeredOnLoadedEntry = List.of(
            TestEntry.class // add your loader class here
    );

// ...ignored
}
```

### 3. Write your xposed hook code
### Java
```java
import java.lang.reflect.Method;

import art.qqlittleice.xposedcompat.transition.BridgeApi;
import art.qqlittleice.xposedcompat.transition.LoadedPackageParam;
import art.qqlittleice.xposedcompat.transition.ModulePackageParam;
import art.qqlittleice.xposedcompat.transition.UniversalBridge;
import art.qqlittleice.xposedloader.UniversalLoader;

public class TestEntry extends UniversalLoader {

    // ... ignored

    @Override
    public void onPackageLoaded(ModulePackageParam modulePackageParam, LoadedPackageParam loadedPackageParam) {
        try {
            Class<?> exampleClass = loadedPackageParam.getClassLoader().loadClass("com.example.someclass");
            Method exampleMethod = exampleClass.getDeclaredMethod("someMethod", String.class);
            bridge.hook(exampleMethod, new BridgeApi.Hooker<>() {
                @Override
                public void beforeMethodInvoked(BridgeApi.BeforeMethodInvokedCallback<Method> callback) {
                    // do something before the method is invoked
                }

                @Override
                public void afterMethodInvoked(BridgeApi.AfterMethodInvokedCallback<Method> callback) {
                    // do something after the method is invoked
                }
            });
        } // ... ignored
    }
    
}
```
### Kotlin
```kotlin
import art.qqlittleice.xposedcompat.transition.BridgeApi
import art.qqlittleice.xposedcompat.transition.LoadedPackageParam
import art.qqlittleice.xposedcompat.transition.ModulePackageParam
import art.qqlittleice.xposedcompat.transition.UniversalBridge
import art.qqlittleice.xposedcompat.util.hookMethod
import art.qqlittleice.xposedloader.UniversalLoader
import java.lang.reflect.Method

class TestEntry(bridge: UniversalBridge): UniversalLoader(bridge) {

    override fun onPackageLoaded(modulePackageParam: ModulePackageParam, loadedPackageParam: LoadedPackageParam) {
        val exampleClass = loadedPackageParam.classLoader.loadClass("com.example.someclass")
        val exampleMethod = exampleClass.getDeclaredMethod("someMethod", String::class.java)
        bridge.hook(exampleMethod, object : BridgeApi.Hooker<Method> {
            override fun beforeMethodInvoked(callback: BridgeApi.BeforeMethodInvokedCallback<Method>) {
                // do something before the method is invoked
            }

            override fun afterMethodInvoked(callback: BridgeApi.AfterMethodInvokedCallback<Method>) {
                // do something after the method is invoked
            }

        })
        
        // or using Kotlin Xposed Helper
        "com.example.someclass".hookMethod(loadedPackageParam.classLoader, "someMethod", String::class.java, object : BridgeApi.Hooker<Method> {
            override fun beforeMethodInvoked(callback: BridgeApi.BeforeMethodInvokedCallback<Method>) {
                // do something before the method is invoked
            }

            override fun afterMethodInvoked(callback: BridgeApi.AfterMethodInvokedCallback<Method>) {
                // do something after the method is invoked
            }
        })
    }
}
```
Done~! You are ready to run your xposed module.
