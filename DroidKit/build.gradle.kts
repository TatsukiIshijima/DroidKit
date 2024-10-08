plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("maven-publish")
}

android {
  namespace = "com.tatsuki.droidkit"
  compileSdk = 34

  defaultConfig {
    minSdk = 21
    targetSdk = 34

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

dependencies {

  implementation("com.jakewharton.timber:timber:5.0.1")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")

  testImplementation("junit:junit:4.13.2")

  androidTestImplementation("androidx.test.ext:junit:1.1.3")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

publishing {
  publications {
    register<MavenPublication>("release") {
      groupId = "com.github.TatsukiIshijima"
      artifactId = "DroidKit"
      version = "1.0.0"

      afterEvaluate {
        from(components["release"])
      }
    }
  }
}