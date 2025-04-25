pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "CitiesList"
include(":app")
include(":domain")
include(":designsystem")
include(":datasource")
include(":datasourcetest")
include(":database")
include(":databaseinitialization")
include(":databasetest")
include(":repository")
include(":querieshistory")
include(":features:citymap")
include(":features:starredcities")
include(":features:citiessearch")
