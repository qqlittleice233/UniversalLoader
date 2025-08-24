pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://api.xposed.info")
    }
}

rootProject.name = "UniversalLoader"
include(":app")
include(":loader:XposedLoader")
include(":loader:XposedApi:modern:api")
include(":loader:XposedApi:modern:service")
include(":loader:XposedCompat")
include(":loader:hiddenapi-stub")
