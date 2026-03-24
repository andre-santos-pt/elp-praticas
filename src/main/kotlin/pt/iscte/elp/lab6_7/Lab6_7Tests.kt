package pt.iscte.elp.lab6_7

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.ByteArrayOutputStream
import java.io.PrintStream


class Lab6_7Tests {
    val src =
        """param max
evenSum := 0
oddMul := 1
i := 0
while i < max {
    # check if even
    if i % 2 = 0 {
        evenSum := evenSum + i
    }
    else {
        oddMul := oddMul * i
    }
    i := i + 1
}
print evenSum
print oddMul
"""

    @Test
    fun `parse and AST to code`() {
        val lexer = SintraGrammarLexer(CharStreams.fromString(src))
        val parser = SintraGrammarParser(CommonTokenStream(lexer))
        val parseTree = parser.script()
        val ast = parseTree.toAST()

        val srcWithoutComments = src.lines().toMutableList().apply {
            removeAt(5)
        }.joinToString("\n")

        assertEquals(srcWithoutComments, ast.toCode())
    }

    @Test
    fun `var not found error`() {
        val srcWithoutLine2 = src.lines().toMutableList().apply {
            removeAt(1)
        }.joinToString("\n")
        val exc = assertThrows<RuntimeException> {
            sintraRun(srcWithoutLine2, "max" to 10)
        }
        println(exc.message)
        assertTrue(exc.message?.contains("variable not found: evenSum") == true)

    }

    @Test
    fun `end to end`() {
        // redirect sysout
        val out = ByteArrayOutputStream()
        val newOut = PrintStream(out)
        System.setOut(newOut)

        sintraRun(src, "max" to 10)
        assertEquals("20\n945\n", out.toString("UTF-8"))
    }

    @Test
    fun `end to end break`() {
        val srcBreak =
            """param max
evenSum := 0
oddMul := 1
i := 0
# infinite loop
while 0 = 0 {
    if i % 2 = 0 {
        evenSum := evenSum + i
    }
    else {
        oddMul := oddMul * i
    }
    i := i + 1
    if i = max {
        break
    }
}
print evenSum
print oddMul
"""
        // redirect sysout
        val out = ByteArrayOutputStream()
        val newOut = PrintStream(out)
        System.setOut(newOut)

        sintraRun(srcBreak, "max" to 10)
        assertEquals("20\n945\n", out.toString("UTF-8"))
    }

    @Test
    fun `invalid break`() {
        val srcBreak =
            """param n
i := 0
if i = n {
    break
}
"""
        val exc = assertThrows<RuntimeException> {
            sintraRun(srcBreak, "n" to 10)
        }
        println(exc.message)
        assertTrue(exc.message?.contains("break must be used within the scope of a while loop") == true)

    }

}