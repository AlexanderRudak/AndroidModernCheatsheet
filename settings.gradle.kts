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
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AndroidModernCheatsheet"
include(":app")
//include(":core_ui")
//include(":core_common")
include(":core:ui")
include(":core:common")
//include(":domain_model")
//include(":domain_repository")
//include(":domain_usecase")
include(":domain:model")
include(":domain:repository")
include(":domain:usecase")
//include(":data_repository")
include(":data:repository")
//include(":feature_mvvm")
include(":feature:mvvm")
include(":core:datastore")
include(":core:network")
include(":data:local")
include(":feature:mvi")
