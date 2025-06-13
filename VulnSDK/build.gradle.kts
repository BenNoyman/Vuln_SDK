plugins {
    alias(libs.plugins.android.library)
    id("maven-publish")
}

android {
    namespace = "com.example.vulnsdk"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    
    // Networking dependencies
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.gson)
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                
                groupId = "com.github.BenNoyman"
                artifactId = "Vuln_SDK"
                version = "1.0.0"
                
                pom {
                    name.set("VulnSDK2")
                    description.set("A powerful Android SDK for vulnerability scanning and security monitoring")
                    url.set("https://github.com/BenNoyman/Vuln_SDK")
                    
                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }
                    
                    developers {
                        developer {
                            id.set("BenNoyman")
                            name.set("Ben Noyman")
                        }
                    }
                    
                    scm {
                        connection.set("scm:git:git://github.com/BenNoyman/Vuln_SDK.git")
                        developerConnection.set("scm:git:ssh://github.com:BenNoyman/Vuln_SDK.git")
                        url.set("https://github.com/BenNoyman/Vuln_SDK")
                    }
                }
            }
        }
    }
}