plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
}

kotlin {

    androidLibrary {
        namespace = "valera.app.core.domain"
        compileSdk = 36
        minSdk = 26
    }

    val xcfName = "core:domainKit"

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
                implementation(libs.koin.core)

                implementation(libs.kotlinx.coroutines.core)

                implementation(libs.kotlin.stdlib)

                implementation(libs.androidx.paging.compose)
                implementation(libs.androidx.paging.common)
            }
        }

        androidMain {
            dependencies {
            }
        }

        iosMain {
            dependencies {
            }
        }
    }

}