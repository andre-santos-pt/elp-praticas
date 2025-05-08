package pt.iscte.elp.lab3

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream


fun validateCSVLinesEqualLength(csv: String): Boolean {
    val lexer = CsvGrammarLexer(CharStreams.fromString(csv))
    val parser = CsvGrammarParser(CommonTokenStream(lexer))

    var lineLengths = mutableSetOf<Int>()
    parser.addParseListener(object : CsvGrammarBaseListener() {
        override fun exitLine(ctx: CsvGrammarParser.LineContext) {
            lineLengths.add(ctx.element().size)
        }
    })
    parser.csv()
    return lineLengths.size == 1
}

fun collectStrings(csv: String): Set<String> {
    val lexer = CsvGrammarLexer(CharStreams.fromString(csv))
    val parser = CsvGrammarParser(CommonTokenStream(lexer))

    val strings = mutableSetOf<String>()
    parser.addParseListener(object : CsvGrammarBaseListener() {
        override fun exitElement(ctx: CsvGrammarParser.ElementContext) {
            if(ctx.STRING() != null)
                strings.add(ctx.STRING().text.trim('\"'))
        }
    })
    parser.csv()
    return strings
}