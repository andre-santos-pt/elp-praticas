package pt.iscte.elp.lab2

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

fun main() {
    val lexer = Expressions(CharStreams.fromString("1 * (2 + 3.4)"))
    val stream = CommonTokenStream(lexer)
    stream.fill()
    stream.tokens.forEach {
        val typeName = lexer.vocabulary.getSymbolicName(it.type)
        println("${it.text} ($typeName)")
    }
}