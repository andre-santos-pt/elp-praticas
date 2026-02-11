package pt.iscte.elp.lab4

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import pt.iscte.elp.lab3.CsvGrammarLexer
import pt.iscte.elp.lab3.CsvGrammarParser
import java.io.File
import java.io.PrintWriter

fun main(args: Array<String>) {
    val src = File(args[0]).readText()
    val ast = parse(src)
    val writer = PrintWriter(args[1])
    writer.println(ast.prettyPrint())
    writer.close()
}

fun parse(text: String): CSV {
    val lexer = CsvGrammarLexer(CharStreams.fromString(text))
    val parser = CsvGrammarParser(CommonTokenStream(lexer))
    val csvContext = parser.csv()
    return csvContext.toAst()
}
