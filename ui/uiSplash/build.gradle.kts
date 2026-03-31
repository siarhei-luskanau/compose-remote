plugins {
    id("composeMultiplatformConvention")
}

kotlin {
    android.namespace = "template.ui.splash"
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.coreCommon)
            implementation(projects.ui.uiCommon)
        }
    }
}
