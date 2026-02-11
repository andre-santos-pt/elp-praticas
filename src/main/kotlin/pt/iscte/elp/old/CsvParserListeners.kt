package pt.iscte.elp.old

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import pt.iscte.elp.lab3.CsvGrammarBaseListener
import pt.iscte.elp.lab3.CsvGrammarLexer
import pt.iscte.elp.lab3.CsvGrammarParser

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

fun validateCSVLinesEqualLength(csv: String): Boolean {
    val lexer = CsvGrammarLexer(CharStreams.fromString(csv))
    val parser = CsvGrammarParser(CommonTokenStream(lexer))

    var lineLengths = mutableSetOf<Int>()
    parser.addParseListener(object : CsvGrammarBaseListener() {
        override fun exitLine(ctx: CsvGrammarParser.LineContext) {
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
        override fun exitValue(ctx: CsvGrammarParser.ValueContext) {
            if(ctx.STRING() != null)
                strings.add(ctx.STRING().text.trim('\"'))
        }
    })
    parser.csv()
    return strings
}




class Lab3Tests {
    @Test
    fun testLineValidation() {
        val csv1 = """
        1,2,3
        1,2,3,4
        1,2,3
        """.trimIndent()

        assertFalse(validateCSVLinesEqualLength(csv1))

        val csv2 = """
        1,2,3,4
        1,2,3,4
        1,2,3,4
        """.trimIndent()
        assertTrue(validateCSVLinesEqualLength(csv2))

        assertTrue(validateCSVLinesEqualLength(""))
    }

    @Test
    fun collectStrings() {
        val csv = """
            "A",10,1
            "B","A", ,true
            "C","B","A"
        """.trimIndent()
        assertEquals(setOf("A", "B", "C"), collectStrings(csv))
    }
}