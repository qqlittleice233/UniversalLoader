package art.qqlittleice.xposedloader;

import android.util.Log;

import com.example.universalloader.XposedEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import art.qqlittleice.xposedcompat.transition.LoadedPackageParam;
import art.qqlittleice.xposedcompat.transition.ModulePackageParam;
import art.qqlittleice.xposedcompat.transition.SystemServerLoadedParam;
import art.qqlittleice.xposedcompat.transition.UniversalBridge;

public abstract class UniversalLoader {

    private static final List<Class<? extends UniversalLoader>> registeredOnLoadedEntry = List.of(
            XposedEntry.class
    );

    private static final HashMap<Class<? extends UniversalLoader>, Object> entryInstance = new HashMap<>();


    private static final String TAG = "UniversalLoader";
    public static void packageLoaded(ModulePackageParam modulePackageParam, LoadedPackageParam loadedPackageParam) {
        Log.d(TAG, "UniversalLoader on loaded!");
        registeredOnLoadedEntry.forEach(loader -> {
            try {
                UniversalLoader instance;
                if (entryInstance.containsKey(loader)) {
                    instance = (UniversalLoader) entryInstance.get(loader);
                } else {
                    instance = loader.getDeclaredConstructor(UniversalBridge.class).newInstance(UniversalBridge.get());
                    entryInstance.put(loader, instance);
                }
                Objects.requireNonNull(instance).onPackageLoaded(modulePackageParam, loadedPackageParam);
            } catch (Throwable e) {
                Log.d(TAG, "onPackageLoaded: " + e.getMessage() + " failed", e);
            }
        });

    }

    public static void systemServerLoaded(SystemServerLoadedParam systemServerLoadedParam) {
        Log.d(TAG, "UniversalLoader on system server loaded!");
        registeredOnLoadedEntry.forEach(loader -> {
            try {
                UniversalLoader instance;
                if (entryInstance.containsKey(loader)) {
                    instance = (UniversalLoader) entryInstance.get(loader);
                } else {
                    instance = loader.getDeclaredConstructor(UniversalBridge.class).newInstance(UniversalBridge.get());
                    entryInstance.put(loader, instance);
                }
                Objects.requireNonNull(instance).onSystemServerLoaded(systemServerLoadedParam);
            } catch (Throwable e) {
                Log.d(TAG, "onSystemServerLoaded: " + e.getMessage() + " failed", e);
            }
        });
    }

    public UniversalBridge bridge = UniversalBridge.get();

    public void onPackageLoaded(ModulePackageParam modulePackageParam, LoadedPackageParam loadedPackageParam) {
    }

    public void onSystemServerLoaded(SystemServerLoadedParam systemServerLoadedParam) {
    }

    public UniversalLoader(UniversalBridge bridge) {
    }
}
