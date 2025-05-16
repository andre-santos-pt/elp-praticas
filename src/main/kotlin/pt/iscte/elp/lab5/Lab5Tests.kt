package pt.iscte.elp.lab5

import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class Lab5Tests {
    val types = StaticJavaParser.parse(File("src/main/resources/Lab5Test.java")).types

    @Test
    fun testNamings() {
        val expected = listOf(
            "Range" to 2,
            "Range" to 5,
            "Rango" to 6,
            "test" to 19,
            "test" to 39,
            "test" to 51
        )
        val warnings = types.flatMap { checkNameConventions(it as ClassOrInterfaceDeclaration) }
        assertEquals(6, warnings.size)
        expected.forEach { warn -> warnings.any {
            it.type == warn.first && it.line == warn.second }
        }
    }

    @Test
    fun testQuality() {
        val expected = listOf(
            "test" to 24,
            "test" to 31,
            "test" to 47,
            "test" to 61
        )
        val warnings = types.flatMap { checkQuality(it as ClassOrInterfaceDeclaration) }
        assertEquals(4, warnings.size)
        expected.forEach { warn -> warnings.any {
            it.type == warn.first && it.line == warn.second }
        }
    }
}