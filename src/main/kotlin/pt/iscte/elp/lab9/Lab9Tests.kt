package pt.iscte.elp.lab9

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class Lab9Tests {
    val NL = System.lineSeparator()

    private fun redirectOutput(): ByteArrayOutputStream {
        val out = ByteArrayOutputStream()
        val newOut = PrintStream(out)
        System.setOut(newOut)
        return out
    }

    private fun runMain(src: String, vararg args: String): ByteArrayOutputStream {
        val out = redirectOutput()
        val clazz = Sintra2Bytecode(src).getClassFile("TestScript")
        clazz.getMethod("main", Array<String>::class.java)
            .invoke(null, args)
        return out
    }

    @Test
    fun testSimple() {
        val src =
            """
i := 0
while i < 5 {
    print i
    i := i + 1
}
"""
        val out = runMain(src)
        assertEquals("0${NL}1${NL}2${NL}3${NL}4${NL}", out.toString("UTF-8"))
    }

    @Test
    fun testArg() {

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
        val out = runMain(src,"10")
        assertEquals("20${NL}945${NL}", out.toString("UTF-8"))
    }

    @Test
    fun testArgs() {

        val src =
            """param min
param max
sum := 0
i := min
while i <= max {
    sum := sum + i
    i := i + 1
}
print sum
"""
        val out = runMain(src,"5", "10")
        assertEquals("45${NL}", out.toString("UTF-8"))
    }

    @Test
    fun testBreak() {

        val src =
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
        val out = runMain(src,"10")
        assertEquals("20${NL}945${NL}", out.toString("UTF-8"))
    }


    @Test
    fun testBreak2() {

        val src =
            """
i := 0
j := 0
while i < 10 {
    while j < 10 {
        if j = 5 {
            break
        }
        j := j + 1
    }
    i := i + 1
}
print i
print j
"""
        val out = runMain(src)
        assertEquals("10${NL}5${NL}", out.toString("UTF-8"))
    }
}