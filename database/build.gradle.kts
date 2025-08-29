plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.room)
}

kotlin {
    jvm()

    androidLibrary {
        namespace = "com.reringuy.database"
        compileSdk = 36
        minSdk = 26
    }

    val xcfName = "databaseKit"

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

    room {
        schemaDirectory("$projectDir/schemas")
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                api(libs.androidx.room.runtime)
                implementation(libs.sqlite.bundled)
                implementation(libs.kotlinx.datetime)

                implementation(libs.bundles.koin)
                implementation(project.dependencies.platform(libs.koin.bom))
                // Add KMP dependencies here
            }
        }

        androidMain {
            dependencies {
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

    dependencies {
        add("kspAndroid", libs.androidx.room.compiler)
        add("kspJvm", libs.androidx.room.compiler)
        add("kspIosSimulatorArm64", libs.androidx.room.compiler)
        add("kspIosX64", libs.androidx.room.compiler)
        add("kspIosArm64", libs.androidx.room.compiler)
        add("kspCommonMainMetadata", libs.koin.ksp.compiler)
    }

    ksp {
        arg("koin.generated", "true")
        arg("KOIN_DEFAULT_MODULE", "false")
        arg("KOIN_CONFIG_CHECK","true")
    }

    targets.configureEach {
        compilations.configureEach {
            compileTaskProvider.get().compilerOptions {
                freeCompilerArgs.addAll(
                    "-Xexpect-actual-classes",
                )
            }
        }
    }

    targets.configureEach {
        compilations.configureEach {
            compileTaskProvider.get()
                .compilerOptions { freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime") }
        }
    }
}