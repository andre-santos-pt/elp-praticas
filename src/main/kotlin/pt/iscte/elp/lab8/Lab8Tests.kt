package pt.iscte.elp.lab8

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ByteArrayClassLoader(parent: ClassLoader? = getSystemClassLoader()) :
    ClassLoader(parent) {

    fun define(name: String, bytes: ByteArray): Class<*> {
        return defineClass(name, bytes, 0, bytes.size)
    }
}

class Lab8Tests {
    val clazz = ByteArrayClassLoader().define("Lab8", generateClassBytes())

    fun call(method: String, vararg arg: Int) =
        clazz.getMethod(method, *arg.map { Int::class.java }.toTypedArray()).invoke(null, *arg.toTypedArray())

    @Test
    fun testInc() {
        assertEquals(31, call("inc", 30))
        assertEquals(-1, call("inc", -2))
    }

    @Test
    fun testIsPositive() {
        assertEquals(true, call("isPositive", 30))
        assertEquals(false, call("isPositive", -3))
    }

    @Test
    fun testFirstDigit() {
        assertEquals(2, call("firstDigit", 2026))
        assertEquals(7, call("firstDigit", 7))
    }

    @Test
    fun testIsEven() {
        assertEquals(true, call("isEven", 16))
        assertEquals(false, call("isEven", 13))
    }

    @Test
    fun testFactorial() {
        assertEquals(24, call("fact", 4))
        assertEquals(1, call("fact", 1))
        assertEquals(1, call("fact", 0))

        assertEquals(24, call("factRec", 4))
        assertEquals(1, call("factRec", 1))
        assertEquals(1, call("factRec", 0))
    }

    @Test
    fun testSumInterval() {
        assertEquals(18, call("sumInterval", 3, 6))
        assertEquals(0, call("sumInterval", 3, 2))
    }
}