package pt.iscte.elp.lab2

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.awt.Color
import java.io.File

fun main(args: Array<String>) {
    val viewer = StyledTextViewer()
    viewer.open(File(args[0]))

    val lexer = JavaLexer(CharStreams.fromString(viewer.text))
    val stream = CommonTokenStream(lexer)
    stream.fill()
    stream.tokens.forEachIndexed { i, it ->
        if (it.type == JavaLexer.KEYWORD || it.type == JavaLexer.PRIMITIVE)
            viewer.setColor(
                it.startIndex,
                it.stopIndex,
                Color.BLUE
            )
        else if (it.type == JavaLexer.OPENCURL || it.type == JavaLexer.CLOSECURL ||
            it.type == JavaLexer.ID && (
                    stream.tokens.getOrNull(i-1)?.type == JavaLexer.ID ||
                            stream.tokens.getOrNull(i-1)?.text == "class" ||
                            stream.tokens.getOrNull(i-1)?.type == JavaLexer.PRIMITIVE
                    )
        )
            viewer.setBold(it.startIndex, it.stopIndex)
        else if (it.type == JavaLexer.STRING)
            viewer.setColor(it.startIndex, it.stopIndex, Color.BLUE)
    }
}