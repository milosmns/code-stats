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

plugins {
  application
  kotlin("multiplatform") version "1.9.+" // when replacing, search the whole file for "9"
  kotlin("plugin.serialization") version "1.9.+"
  id("com.apollographql.apollo3") version "4.+"
  id("com.github.johnrengelman.shadow") version "8.+"
  id("org.jlleitschuh.gradle.ktlint") version "11.+"
  id("com.github.breadmoirai.github-release") version "2.+"
  id("app.cash.sqldelight") version "2.+"
}

repositories {
  mavenCentral()
}

application {
  mainClass.set("MainKt")
}

val env = Env()
val output = Output(env)
val configurator = Configurator(env, output)

group = output.group
version = output.version

kotlin {
  val jvmTarget = configurator.configureJvmTarget(this)
  val nativeTarget = configurator.configureNativeTarget(this)

  sourceSets {

    val commonMain by getting {
      dependencies {
        implementation(kotlin("stdlib-common"))
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.+")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.+")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.+")
        implementation("io.ktor:ktor-client-core:2.3.+")
        implementation("io.ktor:ktor-client-auth:2.3.+")
        implementation("io.ktor:ktor-client-logging:2.3.+")
        implementation("io.ktor:ktor-client-serialization:2.3.+")
        implementation("io.ktor:ktor-client-content-negotiation:2.3.+")
        implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.+")
        implementation("com.squareup.okio:okio:3.4.+")
        implementation("net.mamoe.yamlkt:yamlkt:0.+")
        implementation("com.apollographql.apollo3:apollo-api:4.+")
        implementation("com.apollographql.apollo3:apollo-runtime:4.+")
      }
    }

    val commonTest by getting {
      dependsOn(commonMain)
      dependencies {
        implementation(kotlin("test"))
        implementation("com.squareup.okio:okio-fakefilesystem:3.4.+")
        implementation("com.willowtreeapps.assertk:assertk:0.+")
      }
    }

    val jvmMain by getting {
      dependsOn(commonMain)
      dependencies {
        implementation(kotlin("stdlib"))
        implementation("org.slf4j:slf4j-simple:2.+")
        implementation("io.ktor:ktor-client-cio:2.3.+")
        implementation("app.cash.sqldelight:sqlite-driver:2.+")
      }
    }

    @Suppress("UNUSED_VARIABLE", "KotlinRedundantDiagnosticSuppress") // used by the 'getting' delegate
    val jvmTest by getting {
      dependsOn(commonTest)
      dependsOn(jvmMain)
    }

    val macNativeMain by getting {
      dependsOn(commonMain)
      dependencies {
        implementation("io.ktor:ktor-client-curl:2.3.+")
        implementation("app.cash.sqldelight:native-driver:2.+")
      }
    }

    @Suppress("UNUSED_VARIABLE", "KotlinRedundantDiagnosticSuppress") // used by the 'getting' delegate
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
      archiveBaseName.set(output.artifact)
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
      val distributionsDir = output.getDistributionsDir()
      val artifactName = output.artifact

      val dependentTasks = mutableListOf("shadowJar")
      if (nativeTarget != null) {
        dependentTasks.add("${nativeTarget.name}Binaries")
      }
      dependsOn(*dependentTasks.toTypedArray()) // must be called only once with varargs

      // take native if available
      if (nativeTarget != null) {
        from(output.getBinaryDir(nativeTarget.name)) {
          include("$artifactName.kexe")
          rename { if (it.endsWith(".kexe")) artifactName else it }
        }
      }
      // take JAR anyway
      from(output.getJarDir()) {
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
      val installDir = output.getInstallDir()
      val artifactName = output.artifact

      if (nativeTarget == null) {
        // JVM: copy the file to the root directory
        dependsOn("shadowJar")

        val jarDir = output.getJarDir()
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

        val binaryDir = output.getBinaryDir(nativeTarget.name)
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

sqldelight {
  databases {
    create(output.artifact) {
      packageName.set(output.artifact)
    }
  }
}

githubRelease {
  val writeToken = env.sysVar("GITHUB_TOKEN") { "<invalid>" }
  if (writeToken == "<invalid>") println("Set \$GITHUB_TOKEN to enable GitHub releases.")

  val commitish = env.sysVar("GITHUB_SHA") { "master" }
  val prNumber = env.sysVar("GITHUB_PR_NUMBER")
  val buildMoment = LocalDateTime.now()
  val nowTag = buildMoment.toString("yyyy-MM-dd-HH-mm-ss")
  val quality = env.sysVar("BUILD_QUALITY") { "Development" }
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
  val tag = "${directoryPrefix}${prNumberPrefix}v${output.version}$timestampSuffix"
  val name = "[$quality] v${output.version}"

  token(writeToken)
  owner(output.repoOwner)
  repo(output.repoName)
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
    "(?i).*merge.*",
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

  val distributionsDir = output.getDistributionsDir()
  val jarFile = file("$distributionsDir/${output.artifact}.jar")
  val configFile = file("src/commonMain/resources/sample.config.yaml")
  val unixFile = file("$distributionsDir/${output.artifact}")
  val toInclude = mutableListOf(jarFile, configFile)
  if (unixFile.exists()) toInclude += unixFile
  releaseAssets(*toInclude.toTypedArray())

  println("Configured GitHub release '$tag' for publishing. Run 'githubRelease' to publish.")
}

// region Utils

class Env {
  enum class Platform(val targetName: String) { JVM("jvm"), MAC("macNative") }
  enum class Arch { X86, ARM }

  val currentPlatform = when {
    sysProp("os.name") == "Mac OS X" -> Platform.MAC
    else -> Platform.JVM
  }

  val currentArch = when {
    sysProp("os.arch") in setOf("arm64", "aarch64") -> Arch.ARM
    else -> Arch.X86
  }

  inline fun sysProp(name: String, default: () -> String = { "" }) = System.getProperty(name)
    .takeIf { !it.isNullOrBlank() }
    ?: default()

  inline fun gradleProp(name: String, default: () -> String = { "" }) = providers.gradleProperty(name).orNull
    .takeIf { !it.isNullOrBlank() }
    ?: default()

  inline fun sysVar(name: String, default: () -> String = { "" }) = System.getenv(name)
    .takeIf { !it.isNullOrBlank() }
    ?: default()

}

class Output(private val env: Env) {
  val group = env.sysVar("PROJECT_GROUP") { env.gradleProp("config.group") }
  val artifact = env.sysVar("PROJECT_ARTIFACT") { env.gradleProp("config.artifact") }
  val version = env.sysVar("PROJECT_VERSION") { env.gradleProp("config.version") }
  val repoOwner = env.sysVar("REPO_OWNER") { env.gradleProp("config.gitHubRepoOwner") }
  val repoName = env.sysVar("REPO_NAME") { env.gradleProp("config.gitHubRepoName") }

  fun getInstallDir() = when (env.currentPlatform) {
    Env.Platform.MAC -> "/usr/local/bin"
    else -> env.sysProp("user.dir")
  }.replace('/', File.separatorChar)

  fun getJarDir() = "build/libs"
    .replace('/', File.separatorChar)

  fun getBinaryDir(targetName: String) = "build/bin/$targetName/${artifact}ReleaseExecutable"
    .replace('/', File.separatorChar)

  fun getDistributionsDir() = "build/distributions"
    .replace('/', File.separatorChar)
}

class Configurator(private val env: Env, private val output: Output) {

  fun configureJvmTarget(container: KotlinTargetContainerWithPresetFunctions) =
    container.jvm(Env.Platform.JVM.targetName) {
      compilations.all {
        kotlinOptions.jvmTarget = "17"
        compilerOptions.configure {
          jvmTarget.set(JvmTarget.JVM_17)
        }
      }
      println("Configured Kotlin target '$name'")
    }

  fun configureNativeTarget(container: KotlinTargetContainerWithPresetFunctions) =
    when (env.currentPlatform) {
      Env.Platform.MAC -> when (env.currentArch) {
        Env.Arch.X86 -> container.macosX64(env.currentPlatform.targetName)
        Env.Arch.ARM -> container.macosArm64(env.currentPlatform.targetName)
      }

      else -> null
    }?.also { target ->
      target.binaries {
        executable(output.artifact) {
          entryPoint = "main"
        }
        all {
          binaryOptions["memoryModel"] = "experimental"
        }
      }
      println("Configured Kotlin/Native target '${target.name}'")
    }

}

// endregion
