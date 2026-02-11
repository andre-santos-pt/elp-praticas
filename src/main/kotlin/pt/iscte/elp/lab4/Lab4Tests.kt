package pt.iscte.elp.lab4

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.junit.jupiter.api.Test
import pt.iscte.elp.lab3.CsvGrammarLexer
import pt.iscte.elp.lab3.CsvGrammarParser
import org.junit.jupiter.api.Assertions.*

class Lab4Tests {

    @Test
    fun testAST() {
        val text = """
           "Alan Kay", 1940, ,true
           "Donald Knuth", 1938, ,true
           "Alan Turing", 1912, 1954, false
       """.trimIndent()
        val expected = CSV(listOf(
            Line(listOf(Text("Alan Kay"), Numeric(1940), None, Bool(true))),
            Line(listOf(Text("Donald Knuth"), Numeric(1938), None, Bool(true))),
            Line(listOf(Text("Alan Turing"), Numeric(1912), Numeric(1954), Bool(false)))
        ))
        val lexer = CsvGrammarLexer(CharStreams.fromString(text))
        val parser = CsvGrammarParser(CommonTokenStream(lexer))
        val csvContext = parser.csv()
        val csv = csvContext.toAst()
        assertEquals(expected, csv)
        // ...
    }


    @Test
    fun testPrettyPrint() {
        val lexer = CsvGrammarLexer(CharStreams.fromFileName("src/main/resources/lab4.csv"))
        val parser = CsvGrammarParser(CommonTokenStream(lexer))
        val parseTree = parser.csv()
        val ast = parseTree.toAst()
        val expected = """
            "Alan Kay", 1940,  , true
            "Donald Knuth", 1938,  , true
            "Alan Turing", 1912, 1954, false

        """.trimIndent()
        assertEquals(expected, ast.prettyPrint())
    }
}