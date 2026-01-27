plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "com.example.onboarding"
        compileSdk = 36
        minSdk = 24
    }

    compilerOptions {
        optIn.add("androidx.compose.material3.ExperimentalMaterial3ExpressiveApi")
    }

    val xcfName = "feature:onBoardingKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

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
                dependencies {
                    implementation(libs.compose.runtime)
                    implementation(libs.compose.foundation)
                    implementation(libs.compose.material3)
                    implementation(libs.compose.ui)
                    implementation(libs.compose.components.resources)
                    implementation(libs.compose.ui.tooling.preview)

                    implementation(libs.compose.navigation3.ui)
                    implementation(libs.compose.lifecycle.viewmodel.nav3)

                    implementation(libs.kotlin.stdlib)
                    implementation(libs.kotlinx.coroutines.core)

                    implementation(libs.koin.core)
                    implementation(libs.koin.compose)
                    implementation(libs.koin.compose.viewmodel)

                    implementation(project(":core:ui"))
                    implementation(project(":core:domain"))
                    implementation(project(":feature:shared"))
                }
            }
        }
        androidMain {
            dependencies{
            }
        }
        iosMain {
            dependencies {
            }
        }
    }
}