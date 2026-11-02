pluginManagement {
    repositories {
        google()
        // Google's mirror of Maven Central. Maven Central sometimes rate-limits
        // shared cloud machines (HTTP 429), and this mirror does not.
        maven("https://maven-central.storage-download.googleapis.com/maven2/")
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        // Google's mirror of Maven Central. Maven Central sometimes rate-limits
        // shared cloud machines (HTTP 429), and this mirror does not.
        maven("https://maven-central.storage-download.googleapis.com/maven2/")
        mavenCentral()
    }
}
rootProject.name = "TipJar"
include(":app")
