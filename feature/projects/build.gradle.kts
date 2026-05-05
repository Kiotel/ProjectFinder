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
        namespace = "valera.app.projects"
        compileSdk = 36
        minSdk = 24
    }

    compilerOptions {
        optIn.add("androidx.compose.material3.ExperimentalMaterial3ExpressiveApi")
    }

    val xcfName = "feature:projectsKit"

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
                implementation(libs.kotlin.stdlib)
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.ui.tooling.preview)

                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.androidx.lifecycle.runtime)

                implementation(libs.compose.navigation3.ui)
                implementation(libs.compose.lifecycle.viewmodel.nav3)

                implementation(libs.haze)
                implementation(libs.haze.materials)

                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlinx.coroutines.core)

                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)

                implementation(libs.androidx.paging.compose)
                implementation(libs.androidx.paging.common)

                implementation(project(":core:ui"))
                implementation(project(":core:domain"))
                implementation(project(":feature:shared"))
            }
        }
        androidMain {
            dependencies {

                implementation(libs.ui.tooling)
            }
        }
        iosMain {
            dependencies {
            }
        }
    }
}

compose.resources {
    this.generateResClass = ResourcesExtension.ResourceClassGeneration.Never
}