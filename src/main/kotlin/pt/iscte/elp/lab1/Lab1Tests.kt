package pt.iscte.elp.lab1

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class Lab1Tests {
    @Test
    fun testBinaryNumber() {
        assertTrue(binary.matches("010011"))
        assertFalse(binary.matches(""))
        assertFalse(binary.matches("20"))
    }

    @Test
    fun testAccessModifier() {
        assertTrue(accessModifier.matches("public"))
        assertTrue(accessModifier.matches("protected"))
        assertTrue(accessModifier.matches("private"))
        assertFalse(accessModifier.matches("package"))
        assertFalse(accessModifier.matches(""))
    }

    @Test
    fun testJavaIdentifier() {
        assertTrue(javaIdentifier.matches("id"))
        assertFalse(javaIdentifier.matches(""))
        assertFalse(javaIdentifier.matches("12a"))
    }

    @Test
    fun testXmlTag() {
        assertTrue(xmlTag.matches("<tag>"))
        assertFalse(xmlTag.matches("/tag"))
        assertTrue(xmlTag.matches("</tag>"))
    }

    @Test
    fun testNumber() {
        assertTrue(number.matches("1"))
        assertTrue(number.matches("-2"))
        assertTrue(number.matches("3.2"))
        assertTrue(number.matches("0.234"))
        assertFalse(number.matches(".2"))
        assertFalse(number.matches("7."))
    }

    @Test
    fun testDate() {
        assertTrue(date.matches("22-01-1982"))
        assertFalse(date.matches("74-04-25"))
    }


    @Test
    fun testPhoneNumber() {
        assertTrue(phoneNumber.matches("(+351) 210123123"))
        assertTrue(phoneNumber.matches("(+351)210123123"))
        assertTrue(phoneNumber.matches("(+1)  210123123"))
        assertTrue(phoneNumber.matches("(+47)210123123"))
    }

    @Test
    fun testString() {
        assertTrue(string.matches("\"\""))
        assertTrue(string.matches("\"abc sa ew, 32!\""))
        assertFalse(string.matches("la la la"))
    }

    @Test
    fun testEmail() {
        assertTrue(email.matches("email@example.com"))
        assertTrue(email.matches("email@example"))
        assertFalse(email.matches("emailexample"))
        assertFalse(email.matches("email@@example"))
        assertFalse(email.matches("email@example."))
    }

    @Test
    fun testPositiveList() {
        assertTrue(positiveIntList.matches("1,2,3,4"))
        assertTrue(positiveIntList.matches("1, 2 ,3  ,    4"))
        assertTrue(positiveIntList.matches("10"))
        assertFalse(positiveIntList.matches(""))
    }

    @Test
    fun testJsonNumberArray() {
        assertTrue(jsonNumberArray.matches("[]"))
        assertTrue(jsonNumberArray.matches("[1.5]"))
        assertTrue(jsonNumberArray.matches("[2.3,-1,0,-3.1]"))
    }
}
