import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests

plugins {
  kotlin("multiplatform") version "1.8.+"
  kotlin("plugin.serialization") version "1.8.+"
}

object Output {
  val group = getEnv("PROJECT_GROUP", default = "xyz.marinkovic.milos")
  val artifact = getEnv("PROJECT_ARTIFACT", default = "codestats")
  val version = getEnv("PROJECT_VERSION", default = "0.1.0")
  val author = getEnv("PROJECT_AUTHOR", default = "milosmns")

  fun getBinaryDir(targetName: String) = "/bin/$targetName/${artifact}ReleaseExecutable/"
    .replace('/', File.separatorChar)

  private fun getEnv(name: String, default: String) = System.getenv(name)
    .takeIf { !it.isNullOrBlank() }
    ?: default
}

group = Output.group
version = Output.version

repositories {
  mavenCentral()
}

kotlin {
  System.getProperty("os.name").let { hostOs ->
    if (hostOs != "Mac OS X" && hostOs != "Linux" && !hostOs.startsWith("Windows")) {
      throw GradleException("Host OS is not supported in Kotlin/Native.")
    }
  }

  mutableListOf<KotlinNativeTargetWithHostTests>().apply {
    add(macosArm64())
    add(macosX64())
    add(linuxX64())
  }.forEach {
    it.binaries {
      executable(Output.artifact) {
        entryPoint = "main"
      }
    }
  }

  sourceSets {
    // shared code for all targets

    val commonMain by getting {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.+")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.+")
        implementation("io.ktor:ktor-client-core:2.3.+")
        implementation("io.ktor:ktor-client-cio:2.3.+")
        implementation("io.ktor:ktor-client-logging:2.3.+")
        implementation("com.squareup.okio:okio:3.+")
        implementation("net.mamoe.yamlkt:yamlkt:0.+")
      }
    }

    val commonTest by getting {
      dependsOn(commonMain)
      dependencies {
        implementation(kotlin("test"))
        implementation("com.squareup.okio:okio-fakefilesystem:3.+")
        implementation("com.willowtreeapps.assertk:assertk:0.+")
      }
    }

    // OS-specific targets

    val macosArm64Main by getting {
      dependsOn(commonMain)
    }

    val macosX64Main by getting {
      dependsOn(commonMain)
    }

    val linuxX64Main by getting {
      dependsOn(commonMain)
    }

    // OS-specific test targets

    val macosArm64Test by getting {
      dependsOn(commonTest)
    }

    val macosX64Test by getting {
      dependsOn(commonTest)
    }

    val linuxX64Test by getting {
      dependsOn(commonTest)
    }

  }

}
