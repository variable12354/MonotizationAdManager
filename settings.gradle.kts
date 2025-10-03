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
        maven(url = "https://android-sdk.is.com/")
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://android-sdk.is.com/")
        maven {
            name = "Google"
            url = uri("https://maven.google.com/")
        }
        maven { url = uri("https://jitpack.io") }
        maven {
            name = "Tapjoy"
            url = uri("https://sdk.tapjoy.com/")
        }
    }
}

rootProject.name = "AdsTriggerCompose"
include(":app")
