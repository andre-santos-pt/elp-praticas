package pt.iscte.elp.lab2

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class Lab2Tests {

    @Test
    fun testBracketValidationPass() {
        val exp = "((2+3) + 1)"
        assertTrue(validateBrackets(exp))
    }

    @Test
    fun testBracketValidationFail() {
        val exp = "(2+3) + 1)"
        assertFalse(validateBrackets(exp))
    }

    @Test
    fun testOperationValidationPass() {
        val exp = "((2+3) + 1)"
        assertTrue(validateOperators(exp))
    }

    @Test
    fun testOperationValidationFail() {
        listOf(
            "*(1+2)",
            "(1+3)/",
            "((2++3) + 1)"
        ).forEach {
            assertFalse(validateOperators(it), it)
        }
    }

    @Test
    fun test1() {
        val exp = "(~(9.0) + (2^4))"
        val result = eval(exp)
        assertEquals(19.0, result)
    }

    @Test
    fun test2() {
        val exp = "(1.0 - (2^-2))"
        val result = eval(exp)
        assertEquals(0.75, result)
    }

    @Test
    fun test3() {
        val exp = "((3.0 / 2) * 4)"
        val result = eval(exp)
        assertEquals(6.0, result)
    }
}