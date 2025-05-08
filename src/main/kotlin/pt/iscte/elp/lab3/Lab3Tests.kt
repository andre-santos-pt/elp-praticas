package pt.iscte.elp.lab3

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
}