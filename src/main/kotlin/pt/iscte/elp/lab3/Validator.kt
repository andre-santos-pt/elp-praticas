package pt.iscte.elp.lab3

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

import pt.iscte.elp.lab3.CsvGrammarParser.*
import pt.iscte.elp.lab4.Bool

fun validateCSVLinesEqualLength(csv: String): Boolean {
    val lexer = CsvGrammarLexer(CharStreams.fromString(csv))
    val parser = CsvGrammarParser(CommonTokenStream(lexer))

    var lineLengths = mutableSetOf<Int>()
    parser.addParseListener(object : CsvGrammarBaseListener() {
        override fun exitLine(ctx: LineContext) {
            lineLengths.add(ctx.value().size)
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
        override fun exitValue(ctx: ValueContext) {
            if(ctx.STRING() != null)
                strings.add(ctx.STRING().text.trim('\"'))
        }
    })
    parser.csv()
    return strings
}

fun isWellFormed(csv: CsvContext) : Boolean {
    for(i in 0 until csv.line().size - 1)
        if(csv.line(i).value().isEmpty() || csv.line(i).value().size != csv.line(i+1).value().size)
            return false
    return true
}

fun CsvContext.isWellFormedExt() =
    this.line().map { it.value().size }.toSet().size == 1


fun ValueContext.type() =
    if(NUMBER() != null) NUMBER().symbol.type
    else if(BOOLEAN() != null) BOOLEAN().symbol.type
    else if(STRING() != null) STRING().symbol.type
    else null

fun isWellTyped(csv: CsvGrammarParser.CsvContext) : Boolean {
    require(isWellFormed(csv))

    for(c in 0 until csv.line(0).value().size) {
        val colTypes = mutableSetOf<Int?>()

        for (l in 1 until csv.line().size)
            colTypes.add(csv.line(l).value(c).type())

        colTypes.remove(null)
        if(colTypes.size > 1)
            return false
    }
    return true
}