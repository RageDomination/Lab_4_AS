// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

dependencies {
    implementation("com.google.android.gms:play-services-wearable:19.0.0")
}

private fun DependencyHandlerScope.implementation(string: String) {}
