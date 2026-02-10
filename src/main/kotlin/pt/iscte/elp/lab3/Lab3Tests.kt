package pt.iscte.elp.lab3

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

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

    @Test
    fun testWellFormed() {
        val csv = """
        1,2,3
        4,5,6
        7,8,9
        """.trimIndent()
        val lexer = CsvGrammarLexer(CharStreams.fromString(csv))
        val parser = CsvGrammarParser(CommonTokenStream(lexer))
        assertTrue(isWellFormed(parser.csv()))
    }

    @Test
    fun testWellFormedFail() {
        val csv = """
        1,2,3
        1,2,3,4
        1,2,3
        """.trimIndent()
        val lexer = CsvGrammarLexer(CharStreams.fromString(csv))
        val parser = CsvGrammarParser(CommonTokenStream(lexer))
        assertFalse(isWellFormed(parser.csv()))
    }

    @Test
    fun testWellTyped() {
        val csv = """
        true,,"a"
        false,5,"b"
        true,8,"c"
        """.trimIndent()
        val lexer = CsvGrammarLexer(CharStreams.fromString(csv))
        val parser = CsvGrammarParser(CommonTokenStream(lexer))
        assertTrue(isWellTyped(parser.csv()))
    }

    @Test
    fun testWellFail() {
        val csv = """
        true,2,"a"
        false,false,1
        true,8,"c"
        """.trimIndent()
        val lexer = CsvGrammarLexer(CharStreams.fromString(csv))
        val parser = CsvGrammarParser(CommonTokenStream(lexer))
        assertFalse(isWellTyped(parser.csv()))
    }
}