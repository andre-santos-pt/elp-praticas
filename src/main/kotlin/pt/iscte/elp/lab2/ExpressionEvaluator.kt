package pt.iscte.elp.lab2

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.Token
import kotlin.math.pow

fun validateBrackets(text: String): Boolean {
    val stack = ArrayDeque<Token>()
    val lexer = Expressions(CharStreams.fromString(text))
    val stream = CommonTokenStream(lexer)
    stream.fill()
    stream.tokens.forEach {
        when (it.type) {
            Expressions.OPEN -> stack.addLast(it)
            Expressions.CLOSE ->
                if (stack.isEmpty() || stack.removeLast().type != Expressions.OPEN)
                    return false
        }
    }
    return stack.isEmpty()
}

fun validateOperators(text: String): Boolean {
    val lexer = Expressions(CharStreams.fromString(text))
    val stream = CommonTokenStream(lexer)
    stream.fill()
    val tokens = stream.tokens.dropLast(1)
    if(tokens.firstOrNull()?.type == Expressions.OPERATOR ||
        tokens.lastOrNull()?.type == Expressions.OPERATOR)
        return false

    for (i in 1 until tokens.size)
        if (tokens[i].type == Expressions.OPERATOR &&
            (tokens[i - 1].type == Expressions.OPERATOR ||
                    tokens[i + 1].type == Expressions.OPERATOR)
        )
            return false
    return true
}

fun eval(exp: String): Double {
    val opStack = ArrayDeque<String>()
    val valStack = ArrayDeque<Double>()

    fun calc() {
        val a = valStack.removeLast()
        val res = when (val op = opStack.removeLast()) {
            "+" -> valStack.removeLast() + a
            "*" -> valStack.removeLast() * a
            "-" -> valStack.removeLast() - a
            "/" -> valStack.removeLast() / a
            "^" -> valStack.removeLast().pow(a)
            else -> throw RuntimeException("invalid operator: $op")
        }
        valStack.addLast(res)
    }

    val lexer = Expressions(CharStreams.fromString(exp))
    val stream = CommonTokenStream(lexer)
    stream.fill()
    stream.tokens.forEach {
        when (it.type) {
            Expressions.OPERATOR -> opStack.addLast(it.text)
            Expressions.NUMBER -> valStack.addLast(it.text.toDouble())
            Expressions.CLOSE -> calc()
        }
    }
    return valStack.last()
}