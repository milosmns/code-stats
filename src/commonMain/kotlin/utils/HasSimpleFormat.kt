package utils

interface HasSimpleFormat {

  val simpleFormat: String

  fun simpleFormat(linePrefix: String = ""): String =
    simpleFormat.lines().joinToString("\n") { "$linePrefix$it" }

}
