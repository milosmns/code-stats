import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmCompilation

// Kudos to JMFayard for figuring out lots of stuff in this file!

plugins {
  application
  kotlin("multiplatform") version "1.8.+"
  kotlin("plugin.serialization") version "1.8.+"
  id("com.apollographql.apollo3") version "4.+"
  id("com.github.johnrengelman.shadow") version "8.+"
}

application {
  mainClass.set("MainKt")
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
  val jvmTarget = jvm {
    compilations.all {
      kotlinOptions.jvmTarget = "1.8"
      compilerOptions.configure {
        jvmTarget.set(JvmTarget.JVM_1_8)
      }
    }
    println("Configured Kotlin/Native target '$name'")
  }
  val jvmMainCompilation = jvmTarget.compilations.getByName<KotlinJvmCompilation>("main")

  // set up Kotlin/Native targets
  val hostOs = System.getProperty("os.name")
  val isArm64 = System.getProperty("os.arch") == "arm64" || System.getProperty("os.arch") == "aarch64"
  val macTargetName = "macNative"
  val nativeTarget = when {
    hostOs == "Mac OS X" && isArm64 -> macosArm64(macTargetName)
    hostOs == "Mac OS X" && !isArm64 -> macosX64(macTargetName)
    // future targets should go here
    else -> null
  }
  nativeTarget?.let { target ->
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

  sourceSets {

    // shared code for all targets

    val commonMain by getting {
      dependencies {
        implementation(kotlin("stdlib-common"))
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.+")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.+")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.+")
        implementation("io.ktor:ktor-client-core:2.3.+")
        implementation("io.ktor:ktor-client-auth:2.3.+")
        implementation("io.ktor:ktor-client-cio:2.3.+")
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

    // cross-platform targets

    val jvmMain by getting {
      dependsOn(commonMain)
      dependencies {
        implementation(kotlin("stdlib-jdk8"))
        implementation("org.slf4j:slf4j-simple:2.+")
      }
    }

    @Suppress("UNUSED_VARIABLE") // used by the 'getting' delegate
    val jvmTest by getting {
      dependsOn(commonTest)
      dependsOn(jvmMain)
    }

    // native targets

    val macNativeMain by getting {
      dependsOn(commonMain)
    }

    @Suppress("UNUSED_VARIABLE") // used by the 'getting' delegate
    val macNativeTest by getting {
      dependsOn(commonTest)
      dependsOn(macNativeMain)
    }

  }

  // make the JVM 'run' task work
  tasks.withType<JavaExec> {
    classpath(
      files(
        jvmMainCompilation.runtimeDependencyFiles,
        jvmMainCompilation.output.allOutputs,
      )
    )
  }

  tasks.withType<ShadowJar> {
    archiveBaseName.set(Output.artifact)
    archiveClassifier.set("")
    archiveVersion.set("")

    from(jvmMainCompilation.output)
    configurations = mutableListOf(
      jvmMainCompilation.compileDependencyFiles,
      jvmMainCompilation.runtimeDependencyFiles,
    )
  }

  tasks.withType<Test> {
    useJUnitPlatform()
  }

  // TODO broken, needs fixing (target name is already lowercase)
  // Task with path 'runDebugExecutablemacNative' not found in root project 'CodeStats'
  tasks.register<Copy>("install") {
    group = "run"
    description = "Build the native executable and install it"
    val destDir = "/usr/local/bin"
    if (nativeTarget == null) throw GradleException("No native target configured")
    val nativeTargetName = nativeTarget.name
    dependsOn("runDebugExecutable$nativeTargetName")
    val targetCamelCase = nativeTargetName.first().lowercaseChar() + nativeTargetName.substring(1)
    val folder = "build/bin/$targetCamelCase/debugExecutable"
    from(folder) {
      include("${Output.artifact}.kexe")
      rename { Output.artifact }
    }
    into(destDir)
    doLast {
      println("$ cp $folder/${Output.artifact}.kexe $destDir/$Output.artifact")
    }
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
