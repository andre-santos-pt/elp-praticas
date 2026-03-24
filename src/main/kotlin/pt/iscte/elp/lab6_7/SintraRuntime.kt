package pt.iscte.elp.lab6_7

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

fun sintraRun(src: String, vararg args: Pair<String, Int>) {
    val lexer = SintraGrammarLexer(CharStreams.fromString(src))
    val parser = SintraGrammarParser(CommonTokenStream(lexer))
    val parseTree = parser.script()
    val ast = parseTree.toAST()
    val errors = ast.validate()
    if(errors.isEmpty())
        Interpreter(ast).run(*args)
    else
        throw RuntimeException(errors.joinToString("\n"))
}