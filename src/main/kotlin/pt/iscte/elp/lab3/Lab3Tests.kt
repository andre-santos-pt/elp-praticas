package pt.iscte.elp.lab3

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Lab3Tests {

    @Test
    fun testWellFormed() {
        val text = """
        1,2,3
        4,5,6
        7,8,9
        """.trimIndent()
        val lexer = CsvGrammarLexer(CharStreams.fromString(text))
        val parser = CsvGrammarParser(CommonTokenStream(lexer))
        val csv = parser.csv()
        assertTrue(isWellFormed(csv))
        assertTrue(csv.isWellFormedExt())
    }

    @Test
    fun testWellFormedFail() {
        val text = """
        1,2,3
        1,2,3,4
        1,2,3
        """.trimIndent()
        val lexer = CsvGrammarLexer(CharStreams.fromString(text))
        val parser = CsvGrammarParser(CommonTokenStream(lexer))
        val csv = parser.csv()
        assertFalse(isWellFormed(csv))
        assertFalse(csv.isWellFormedExt())
    }

    @Test
    fun testWellTyped() {
        val text = """
        true,,"a"
        false,5,"b"
        true,8,"c"
        """.trimIndent()
        val lexer = CsvGrammarLexer(CharStreams.fromString(text))
        val parser = CsvGrammarParser(CommonTokenStream(lexer))
        val csv = parser.csv()
        assertTrue(isWellTyped(csv))
    }

    @Test
    fun testWellFail() {
        val text = """
        true,2,"a"
        false,false,1
        true,8,"c"
        """.trimIndent()
        val lexer = CsvGrammarLexer(CharStreams.fromString(text))
        val parser = CsvGrammarParser(CommonTokenStream(lexer))
        val csv = parser.csv()
        assertFalse(isWellTyped(csv))
    }
}