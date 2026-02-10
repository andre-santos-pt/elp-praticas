package pt.iscte.elp.lab3

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

fun main() {
    val lexer = IdListLexer(CharStreams.fromString("""
        x,y,    z,
        max
    """.trimIndent()))
    val parser = IdListParser(CommonTokenStream(lexer))
    val idList = parser.list()
    for(id in idList.id())
        println(id.text) // x  y  x  max
}
