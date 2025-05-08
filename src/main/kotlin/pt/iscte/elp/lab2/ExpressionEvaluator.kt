package pt.iscte.elp.lab2

import org.antlr.v4.runtime.CharStreams
import java.util.Scanner
import kotlin.math.pow
import kotlin.math.sqrt

fun <T> MutableList<T>.push(e: T) = add(e)
fun <T> MutableList<T>.peek() = last()
fun <T> MutableList<T>.pop() = removeLast()

fun eval(exp: String): Double {
    val opStack = mutableListOf<String>()
    val valStack = mutableListOf<Double>()

    fun calc() {
        val a = valStack.pop()
        val res = when (opStack.pop()) {
            "+" -> valStack.pop() + a
            "*" -> valStack.pop() * a
            "-" -> valStack.pop() - a
            "/" -> valStack.pop() / a
            "^" -> valStack.pop().pow(a)
            "~" -> sqrt(a)
            else -> 0.0
        }
        valStack.push(res)
    }

    val lexer = ExpressionsLexer(CharStreams.fromString(exp))
    lexer.allTokens.forEach {
        when (it.type) {
            ExpressionsLexer.OPERATOR -> opStack.push(it.text)
            ExpressionsLexer.NUMBER -> valStack.push(it.text.toDouble())
            ExpressionsLexer.CLOSE -> calc()
        }
    }
    return valStack.peek()
}