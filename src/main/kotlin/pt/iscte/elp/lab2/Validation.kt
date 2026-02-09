package pt.iscte.elp.lab2

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.Token


fun validateBrackets(text: String): Boolean {
    val stack = ArrayDeque<Token>()
    val lexer = ExpressionsLexer(CharStreams.fromString(text))
    lexer.allTokens.forEach {
        when (it.type) {
            ExpressionsLexer.OPEN -> stack.push(it)
            ExpressionsLexer.CLOSE ->
                if (stack.isEmpty() || stack.pop().type != ExpressionsLexer.OPEN)
                    return false
        }
    }
    return true
}

fun validateOperators(text: String): Boolean {
    val lexer = ExpressionsLexer(CharStreams.fromString(text))
    val stream = CommonTokenStream(lexer)
    stream.fill()
    val tokens = stream.tokens.dropLast(1)
    if(tokens.firstOrNull()?.type == ExpressionsLexer.OPERATOR ||
        tokens.lastOrNull()?.type == ExpressionsLexer.OPERATOR)
        return false

    for (i in 1 until tokens.size)
        if (tokens[i].type == ExpressionsLexer.OPERATOR &&
            (tokens[i - 1].type == ExpressionsLexer.OPERATOR ||
                    tokens[i + 1].type == ExpressionsLexer.OPERATOR)
        )
            return false
    return true
}