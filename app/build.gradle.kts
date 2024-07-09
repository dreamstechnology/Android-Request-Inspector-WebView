plugins {
    id("com.android.library")
    kotlin("android")
    `maven-publish`
    signing
}

val currentVersion = "1.0.6"

group = "com.getdreams"
version = currentVersion

android {
    compileSdk = 31
    namespace = "com.acsbendi.requestinspectorwebview"

    defaultConfig {
        minSdk = 21
        targetSdk = 31

        version = currentVersion
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            repositories {
                maven {
                    name = "GitHubPackages"
                    url = uri("https://maven.pkg.github.com/dreamstechnology/Android-Request-Inspector-WebView")
                    credentials {
                        username =
                            project.findProperty("gpr.user") as? String ?: System.getenv("GITHUB_USERNAME")
                        password = project.findProperty("gpr.key") as? String ?: System.getenv("GITHUB_TOKEN")
                    }
                }
            }
            register<MavenPublication>("release") {
                groupId = group as String
                artifactId = "requestinspectorwebview"
                version = currentVersion

                from(components["release"])
            }
        }
    }

    signing {
        useGpgCmd()
        sign(publishing.publications["release"])
    }
}
