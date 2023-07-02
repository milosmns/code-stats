import com.github.breadmoirai.githubreleaseplugin.ChangeLogSupplier
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.internal.impldep.org.joda.time.LocalDateTime
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
  id("com.github.breadmoirai.github-release") version "2.+"
}

repositories {
  mavenCentral()
}

application {
  mainClass.set("MainKt")
}

group = Out.group
version = Out.version

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

  tasks {

    withType<Test> {
      useJUnitPlatform()
      testLogging {
        exceptionFormat = FULL
        events = setOf(PASSED, FAILED, SKIPPED)
      }
    }

    // make the JVM 'run' task work
    withType<JavaExec> {
      val jvmMainCompilation = jvmTarget.compilations.getByName<KotlinJvmCompilation>("main")
      classpath(
        files(
          jvmMainCompilation.runtimeDependencyFiles,
          jvmMainCompilation.output.allOutputs,
        )
      )
    }

    // make the JVM 'jar' task work
    withType<ShadowJar> {
      archiveBaseName.set(Out.artifact)
      archiveClassifier.set("")
      archiveVersion.set("")

      val jvmMainCompilation = jvmTarget.compilations.getByName<KotlinJvmCompilation>("main")
      from(jvmMainCompilation.output)

      configurations = mutableListOf(
        jvmMainCompilation.compileDependencyFiles,
        jvmMainCompilation.runtimeDependencyFiles,
      )
    }

    register<Copy>("prepareForPublish") {
      group = "distribution"
      description = "Prepare the project outputs for publishing"
      val distributionsDir = Out.getDistributionsDir()
      val artifactName = Out.artifact

      val dependentTasks = mutableListOf("shadowJar")
      if (nativeTarget != null) {
        dependentTasks.add("${nativeTarget.name}Binaries")
      }
      dependsOn(*dependentTasks.toTypedArray()) // must be called only once with varargs

      // take native if available
      if (nativeTarget != null) {
        from(Out.getBinaryDir(nativeTarget.name)) {
          include("$artifactName.kexe")
          rename { if (it.endsWith(".kexe")) artifactName else it }
        }
      }
      // take JAR anyway
      from(Out.getJarDir()) {
        include("$artifactName.jar")
      }

      into(distributionsDir)

      doLast {
        println("Copied $artifactName artifact[s] to $distributionsDir")
      }
    }

    register<Copy>("install") {
      group = "application"
      description = "Build the native executable and install it"
      val installDir = Out.getInstallDir()
      val artifactName = Out.artifact

      if (nativeTarget == null) {
        // JVM: copy the file to the root directory
        dependsOn("shadowJar")

        val jarDir = Out.getJarDir()
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

        val binaryDir = Out.getBinaryDir(nativeTarget.name)
        from(binaryDir) {
          include("$artifactName.kexe")
          rename { artifactName }
        }
        into(installDir)

        doLast {
          println("Copied $binaryDir/$artifactName.kexe to $installDir/$artifactName")
        }
      }
    } // install task

    named("githubRelease") {
      dependsOn("prepareForPublish")
    }

  } // tasks

} // kotlin

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

githubRelease {
  val writeToken = OS.env("GITHUB_TOKEN", "<invalid>")
  if (writeToken == "<invalid>") println("Set \$GITHUB_TOKEN to enable GitHub releases.")

  val commitish = OS.env("GITHUB_SHA", default = "master")
  val prNumber = OS.env("GITHUB_PR_NUMBER", default = "")
  val buildMoment = LocalDateTime.now()
  val nowTag = buildMoment.toString("yyyy-MM-dd-HH-mm-ss")
  val quality = OS.env("BUILD_QUALITY", default = "Development")
  var directoryPrefix = ""
  var prNumberPrefix = ""
  var timestampSuffix = ""
  when (quality) {
    "GA" -> Unit
    "PR" -> {
      directoryPrefix = "pr/"
      prNumberPrefix = if (prNumber.isNotBlank()) "$prNumber/" else ""
      timestampSuffix = "/$nowTag"
    }

    else -> {
      directoryPrefix = "dev/"
      prNumberPrefix = ""
      timestampSuffix = "/$nowTag"
    }
  }
  val tag = "${directoryPrefix}${prNumberPrefix}v${Out.version}$timestampSuffix"
  val name = "[$quality] v${Out.version}"

  token(writeToken)
  owner(Out.repoOwner)
  repo(Out.repoName)
  tagName(tag)
  releaseName(name)
  targetCommitish(commitish)
  prerelease(quality != "GA")

  val maxFetched = 15
  val maxReported = 7
  val bullet = "\n* "
  val changelogConfig = closureOf<ChangeLogSupplier> {
    currentCommit("HEAD")
    lastCommit("HEAD~$maxFetched")
    options("--format=oneline", "--abbrev-commit", "--max-count=$maxFetched")
  }
  val ignoredMessagesRegex = setOf(
    "(?i).*bump.*version.*",
    "(?i).*increase.*version.*",
    "(?i).*version.*bump.*",
    "(?i).*version.*increase.*",
    "(?i).*merge.*request.*",
    "(?i).*request.*merge.*",
  ).map(String::toRegex)

  val changes = try {
    changelog(changelogConfig)
      .call()
      .trim()
      .split("\n")
      .map { it.trim() }
      .filterNot { ignoredMessagesRegex.any(it::matches) }
      .take(maxReported)
  } catch (t: Throwable) {
    System.err.println("Failed to fetch Git history. Reason: ${t.message}")
    emptyList()
  }
  val changesAsBullets = changes.joinToString(separator = bullet, prefix = bullet)

  body(
    when {
      changes.isNotEmpty() -> "## Latest changes\n$changesAsBullets"
      else -> "Explore commit history for latest changes."
    }
  )

  val distributionsDir = Out.getDistributionsDir()
  val jarFile = file("$distributionsDir/${Out.artifact}.jar")
  val configFile = file("src/commonMain/resources/sample.config.yaml")
  val unixFile = file("$distributionsDir/${Out.artifact}")
  val toInclude = mutableListOf(jarFile, configFile)
  if (unixFile.exists()) toInclude += unixFile
  releaseAssets(*toInclude.toTypedArray())

  println("Configured GitHub release '$tag' for publishing. Run 'githubRelease' to publish.")
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

object Out {
  val group = OS.env("PROJECT_GROUP", default = "xyz.marinkovic.milos")
  val artifact = OS.env("PROJECT_ARTIFACT", default = "codestats")
  val version = OS.env("PROJECT_VERSION", default = "0.1.1")
  val repoOwner = OS.env("REPO_OWNER", default = "milosmns")
  val repoName = OS.env("REPO_NAME", default = "code-stats")

  fun getInstallDir() = when (OS.currentPlatform) {
    OS.Platform.MAC -> "/usr/local/bin"
    else -> OS.prop("user.dir")
  }.replace('/', File.separatorChar)

  fun getJarDir() = "build/libs"
    .replace('/', File.separatorChar)

  fun getBinaryDir(targetName: String) = "build/bin/$targetName/${artifact}ReleaseExecutable"
    .replace('/', File.separatorChar)

  fun getDistributionsDir() = "build/distributions"
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
      println("Configured Kotlin target '$name'")
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
        executable(Out.artifact) {
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
