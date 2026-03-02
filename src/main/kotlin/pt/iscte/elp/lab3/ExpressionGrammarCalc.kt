package pt.iscte.elp.lab3

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import kotlin.math.pow

fun ExpressionsGrammarParser.ExpContext.calculate(): Double =
    if (value != null)
        value.text.toDouble()
    else if (expression != null)
        expression.calculate()
    else {
        val left = left.calculate()
        val right = right.calculate()
        if (multOperator()?.MULT() != null)
            left * right
        else if (multOperator()?.DIV() != null)
            left / right
        else if (multOperator()?.POW() != null)
            left.pow(right)
        else if (addOperator()?.PLUS() != null)
            left + right
        else if (addOperator()?.MINUS() != null)
            left - right
        else
            throw RuntimeException("invalid operator: ${multOperator()?.text ?: addOperator()}")
    }

fun calculateExpression(text: String): Double {
    val lexer = ExpressionsGrammarLexer(CharStreams.fromString(text))
    val parser = ExpressionsGrammarParser(CommonTokenStream(lexer))
    val exp = parser.exp()
    return exp.calculate()
}