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
                // Add KMP dependencies here
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

        jvmMain {
            dependencies {

            }
        }
    }

    dependencies {
        add("kspAndroid", libs.androidx.room.compiler)
        add("kspJvm", libs.androidx.room.compiler)
        add("kspIosSimulatorArm64", libs.androidx.room.compiler)
        add("kspIosX64", libs.androidx.room.compiler)
        add("kspIosArm64", libs.androidx.room.compiler)
//        ksp(libs.androidx.room.compiler)
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