plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
}

android {
    defaultConfig {
        namespace = "com.reringuy.feature.clock"
        minSdk = 26
        compileSdk = 36
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

kotlin {
    androidTarget()
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                implementation(libs.jetbrains.adaptive)

                implementation(libs.jetbrains.compose.icons)
                implementation(libs.jetbrains.compose.ui.graphics)

                implementation(libs.kotlinx.datetime)

                implementation(libs.kermit.log)

                implementation(libs.bundles.koin)
                implementation(project.dependencies.platform(libs.koin.bom))

                implementation(libs.jetbrains.material3)
                implementation(libs.jetbrains.material)

                implementation(project(":database"))
                implementation(project(":core:utils"))
                // Add KMP dependencies here
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.activity.compose)
                implementation(libs.koin.core)

            }
        }

        iosMain {
            dependencies {
                implementation(libs.koin.core)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.koin.core)
            }
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", libs.koin.ksp.compiler)
}

ksp {
    arg("koin.generated", "true")
    arg("KOIN_DEFAULT_MODULE", "false")
    arg("KOIN_CONFIG_CHECK","true")
}


tasks.matching {
    it.name in listOf(
        "linkReleaseFrameworkIosArm64",
        "linkReleaseFrameworkIosSimulatorArm64",
        "createReleaseApkListingFileRedirect",
        "linkReleaseFrameworkIosX64",
        "packageRelease"
    )
}.configureEach {
    onlyIf { gradle.startParameter.taskNames.any { it.contains("Release", ignoreCase = true) } }
}