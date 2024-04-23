pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        jcenter()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri("https://jitpack.io") }
        google()
        jcenter()

    }
}

rootProject.name = "CoffeeShopApp"
include(":app")
 