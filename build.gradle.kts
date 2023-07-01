import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinTargetContainerWithPresetFunctions
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmCompilation

// Kudos to JMFayard for figuring out lots of KMP stuff!

plugins {
  application
  kotlin("multiplatform") version "1.8.+"
  kotlin("plugin.serialization") version "1.8.+"
  id("com.apollographql.apollo3") version "4.+"
  id("com.github.johnrengelman.shadow") version "8.+"
  id("org.jlleitschuh.gradle.ktlint") version "11.+"
}

repositories {
  mavenCentral()
}

application {
  mainClass.set("MainKt")
}

group = Output.group
version = Output.version

kotlin {
  val jvmTarget = Configurator.configureJvmTarget(this)
  val nativeTarget = Configurator.configureNativeTarget(this)

  sourceSets {

    val commonMain by getting {
      dependencies {
        implementation(kotlin("stdlib-common"))
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.+")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.+")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.+")
        implementation("io.ktor:ktor-client-core:2.3.+")
        implementation("io.ktor:ktor-client-auth:2.3.+")
        implementation("io.ktor:ktor-client-logging:2.3.+")
        implementation("io.ktor:ktor-client-serialization:2.3.+")
        implementation("io.ktor:ktor-client-content-negotiation:2.3.+")
        implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.+")
        implementation("com.squareup.okio:okio:3.+")
        implementation("net.mamoe.yamlkt:yamlkt:0.+")
        implementation("com.apollographql.apollo3:apollo-api:4.+")
        implementation("com.apollographql.apollo3:apollo-runtime:4.+")
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

    val jvmMain by getting {
      dependsOn(commonMain)
      dependencies {
        implementation(kotlin("stdlib-jdk8"))
        implementation("org.slf4j:slf4j-simple:2.+")
        implementation("io.ktor:ktor-client-cio:2.3.+")
      }
    }

    @Suppress("UNUSED_VARIABLE") // used by the 'getting' delegate
    val jvmTest by getting {
      dependsOn(commonTest)
      dependsOn(jvmMain)
    }

    val macNativeMain by getting {
      dependsOn(commonMain)
      dependencies {
        implementation("io.ktor:ktor-client-curl:2.3.+")
      }
    }

    @Suppress("UNUSED_VARIABLE") // used by the 'getting' delegate
    val macNativeTest by getting {
      dependsOn(commonTest)
      dependsOn(macNativeMain)
    }

  }

  tasks.withType<Test> {
    useJUnitPlatform()
  }

  // make the JVM 'run' task work
  tasks.withType<JavaExec> {
    val jvmMainCompilation = jvmTarget.compilations.getByName<KotlinJvmCompilation>("main")
    classpath(
      files(
        jvmMainCompilation.runtimeDependencyFiles,
        jvmMainCompilation.output.allOutputs,
      )
    )
  }

  // make the JVM 'jar' task work
  tasks.withType<ShadowJar> {
    archiveBaseName.set(Output.artifact)
    archiveClassifier.set("")
    archiveVersion.set("")

    val jvmMainCompilation = jvmTarget.compilations.getByName<KotlinJvmCompilation>("main")
    from(jvmMainCompilation.output)

    configurations = mutableListOf(
      jvmMainCompilation.compileDependencyFiles,
      jvmMainCompilation.runtimeDependencyFiles,
    )
  }

  tasks.register<Copy>("install") {
    group = "application"
    description = "Build the native executable and install it"
    val installDir = Output.getInstallDir()
    val artifactName = Output.artifact

    if (nativeTarget == null) {
      // JVM: copy the file to the root directory
      dependsOn("shadowJar")
      val jarDir = Output.getJarDir()
      from(jarDir) {
        include("$artifactName.jar")
      }
      into(installDir)

      doLast {
        println("Copied $jarDir/$artifactName.jar to $installDir/$artifactName.jar")
      }
    } else {
      // Native: copy the file to the programs directory
      dependsOn("${nativeTarget.name}Binaries")
      val binaryDir = Output.getBinaryDir(nativeTarget.name)
      from(binaryDir) {
        include("$artifactName.kexe")
        rename { artifactName }
      }
      into(installDir)

      doLast {
        println("Copied $binaryDir/$artifactName.kexe to $installDir/$artifactName")
      }
    }
  }

}

ktlint {
  verbose.set(true)
  outputToConsole.set(true)
  enableExperimentalRules.set(true)

  filter {
    exclude { projectDir.toURI().relativize(it.file.toURI()).path.contains("/sqldelight/") }
    exclude { projectDir.toURI().relativize(it.file.toURI()).path.contains("/generated/") }
  }
}

apollo {
  service("github") {
    generateKotlinModels.set(true)
    generateOptionalOperationVariables.set(false)
    packageName.set("com.github.graphql")
    outputDirConnection {
      connectToKotlinSourceSet("commonMain")
    }
  }
}

// region Utils

object OS {
  enum class Platform(val targetName: String) { JVM("jvm"), MAC("macNative") }
  enum class Arch { X86, ARM }

  val currentPlatform = when {
    prop("os.name") == "Mac OS X" -> Platform.MAC
    else -> Platform.JVM
  }

  val currentArch = when {
    prop("os.arch") in setOf("arm64", "aarch64") -> Arch.ARM
    else -> Arch.X86
  }

  fun prop(name: String, default: String = "") = System.getProperty(name)
    .takeIf { !it.isNullOrBlank() }
    ?: default

  fun env(name: String, default: String) = System.getenv(name)
    .takeIf { !it.isNullOrBlank() }
    ?: default
}

object Output {
  val group = OS.env("PROJECT_GROUP", default = "xyz.marinkovic.milos")
  val artifact = OS.env("PROJECT_ARTIFACT", default = "codestats")
  val version = OS.env("PROJECT_VERSION", default = "0.1.0")
  val author = OS.env("PROJECT_AUTHOR", default = "milosmns")

  fun getInstallDir() = when (OS.currentPlatform) {
    OS.Platform.MAC -> "/usr/local/bin"
    else -> OS.prop("user.dir")
  }.replace('/', File.separatorChar)

  fun getJarDir() = "build/libs"
    .replace('/', File.separatorChar)

  fun getBinaryDir(targetName: String) = "build/bin/$targetName/${artifact}ReleaseExecutable"
    .replace('/', File.separatorChar)
}

object Configurator {

  fun configureJvmTarget(container: KotlinTargetContainerWithPresetFunctions) =
    container.jvm(OS.Platform.JVM.targetName) {
      compilations.all {
        kotlinOptions.jvmTarget = "1.8"
        compilerOptions.configure {
          jvmTarget.set(JvmTarget.JVM_1_8)
        }
      }
      println("Configured Kotlin/Native target '$name'")
    }

  fun configureNativeTarget(container: KotlinTargetContainerWithPresetFunctions) =
    when (OS.currentPlatform) {
      OS.Platform.MAC -> when (OS.currentArch) {
        OS.Arch.X86 -> container.macosX64(OS.currentPlatform.targetName)
        OS.Arch.ARM -> container.macosArm64(OS.currentPlatform.targetName)
      }

      else -> null
    }?.also { target ->
      target.binaries {
        executable(Output.artifact) {
          entryPoint = "main"
        }
      }
      target.binaries.all {
        binaryOptions["memoryModel"] = "experimental"
      }
      println("Configured Kotlin/Native target '${target.name}'")
    }

}

// endregion
