package pt.iscte.elp.lab6_7

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

fun sintraAST(src: String): Script {
    val lexer = SintraGrammarLexer(CharStreams.fromString(src))
    val parser = SintraGrammarParser(CommonTokenStream(lexer))
    val parseTree = parser.script()
    return parseTree.toAST()
}

fun sintraRun(src: String, vararg args: Pair<String, Int>) {
    val ast = sintraAST(src)
    val errors = ast.validate()
    if(errors.isEmpty())
        Interpreter(ast).run(*args)
    else
        throw RuntimeException(errors.joinToString("\n"))
}
