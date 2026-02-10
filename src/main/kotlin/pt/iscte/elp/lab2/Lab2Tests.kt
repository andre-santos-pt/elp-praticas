package pt.iscte.elp.lab2

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class Lab2Tests {

    @Test
    fun testBracketValidation() {
        assertTrue(validateBrackets("((2+3) + 1)"))
        assertFalse(validateBrackets("(2+3) + 1)"))
        assertFalse(validateBrackets("(2+3) + (1"))
    }

    @Test
    fun testOperationValidation() {
        assertTrue(validateOperators("((2+3) + 1)"))
        listOf(
            "*(1+2)",
            "(1+3)/",
            "((2++3) + 1)"
        ).forEach {
            assertFalse(validateOperators(it), it)
        }
    }

    @Test
    fun testExpressionEvaluator() {
        assertEquals(1.06, eval("((5.2 + 5.4) / 10)"))
        assertEquals(0.75, eval("(1.0 - (2^-2))"))
        assertEquals(6.0, eval("((3.0 / 2) * 4)"))
        assertEquals(16.0, eval("(((12 * 0.5) + (16 * 0.25)) + (18 / 3))"))
    }

}