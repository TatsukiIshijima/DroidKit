import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("maven-publish")
}

val versionProperties = loadProperties(file("versions.properties").absolutePath)
val versionName = versionProperties.getProperty("VERSION_NAME")

android {
  namespace = "com.tatsuki.droidkit"
  compileSdk = 32

  defaultConfig {
    minSdk = 21
    targetSdk = 32

    aarMetadata {
      minCompileSdk = 21
    }

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
    getByName("debug") {

    }
  }
  libraryVariants.all {
    outputs.forEach { output ->
      if (output !is BaseVariantOutputImpl) {
        return@forEach
      }
      val fileName = when (name) {
        "debug" -> "droidkit-debug-${versionName}.aar"
        else -> "droidkit-${versionName}.aar"
      }
      output.outputFileName = fileName
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  testFixtures {
    enable = true
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
      groupId = "com.github.tatsukiishijima"
      artifactId = "DroidKit"
      version = versionName

      afterEvaluate {
        from(components["release"])
      }
    }
  }
}
