plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "art.qqlittleice.xposedcompat"
    compileSdk = 34

    defaultConfig {
        minSdk = 27
        consumerProguardFiles("consumer-rules.pro")
        externalNativeBuild {
            cmake {
                arguments("-DANDROID_STL=none")
                val flags = listOf(
                    "-funwind-tables",
                    "-fasynchronous-unwind-tables",
                    "-Qunused-arguments",
                    "-fno-rtti",
                    "-fno-exceptions",
                    "-fvisibility=hidden",
                    "-fvisibility-inlines-hidden",
                    "-Wno-unused-value",
                    "-Wno-unused-variable",
                    "-Wno-unused-command-line-argument",
                )
                cFlags += flags
                cppFlags += flags
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            externalNativeBuild {
                cmake {
                    val releaseFlags = listOf(
                        "-ffunction-sections",
                        "-fdata-sections",
                        "-Wl,--gc-sections",
                        "-Wl,--exclude-libs,ALL",
                        "-Wl,--strip-all",
                        "-Wl,-z,max-page-size=16384",
                        "-flto=full",
                    )
                    val configFlags = listOf(
                        "-Oz",
                        "-DNDEBUG"
                    ).joinToString(" ")
                    cFlags += releaseFlags
                    cppFlags += releaseFlags
                    arguments("-DCMAKE_BUILD_TYPE=Release", "-DCMAKE_CXX_FLAGS_RELEASE=$configFlags", "-DCMAKE_C_FLAGS_RELEASE=$configFlags")
                }
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    externalNativeBuild {
        cmake {
            path("src/main/jni/CMakeLists.txt")
        }
    }
    ndkVersion = "28.2.13676358" // r28c
}

dependencies {
    compileOnly(project(":loader:hiddenapi-stub"))
    compileOnly(project(":loader:XposedApi:modern:api"))
    compileOnly(libs.classical.xposed.api)
    compileOnly(libs.androidx.annotation)

}