import org.jetbrains.compose.resources.ResourcesExtension

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {

    androidLibrary {
        namespace = "valera.app.navigation"
        compileSdk = 36
        minSdk = 24
    }

    compilerOptions {
        optIn.add("androidx.compose.material3.ExperimentalMaterial3ExpressiveApi")
    }

    val xcfName = "feature:navigationKit"

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.ui.tooling.preview)
                implementation(libs.material.icons.extended)

                implementation(libs.androidx.lifecycle.runtimeCompose)

                implementation(libs.compose.navigation3.ui)
                implementation(libs.compose.lifecycle.viewmodel.nav3)

                implementation(libs.haze)
                implementation(libs.haze.materials)

                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)

                implementation(project(":core:domain"))
                implementation(project(":core:ui"))
                implementation(project(":feature:shared"))
                implementation(project(":feature:onBoarding"))
                implementation(project(":feature:profile"))
                implementation(project(":feature:auth"))
                implementation(project(":feature:projects"))
            }
        }
    }
}

compose.resources {
    this.generateResClass = ResourcesExtension.ResourceClassGeneration.Never
}