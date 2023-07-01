import okio.Buffer
import okio.FileSystem
import okio.Path
import okio.buffer
import okio.use

actual fun loadFileAsString(configPath: Path): String = Buffer().apply {
  FileSystem.SYSTEM.source(configPath).use { source ->
    source.buffer().readAll(this)
  }
}.readUtf8()

actual fun readEnvVar(name: String): String? = System.getenv(name)
