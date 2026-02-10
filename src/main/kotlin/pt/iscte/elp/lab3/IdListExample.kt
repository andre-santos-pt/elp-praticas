package pt.iscte.elp.lab3

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

import pt.iscte.elp.lab3.IdListParser.* // classes da gramática geradas

fun main() {
    val lexer = IdListLexer(CharStreams.fromString("""
        x,y,    x,
        max
    """.trimIndent()))
    val parser = IdListParser(CommonTokenStream(lexer))
    val idList: ListContext = parser.list() // criar parse tree
    for(e: ElementContext in idList.element())
        println(e.text) // x  y  x  max

    println(distinctIds(idList))
    println(idList.distinctIdsExt())
}

fun distinctIds(list : ListContext): Set<String> {
    val set = mutableSetOf<String>()
    for(e in list.element())
        set.add(e.ID().text)
    return set
}

fun ListContext.distinctIdsExt(): Set<String> =
    this.element()
        .map { it.ID().text }
        .toSet()