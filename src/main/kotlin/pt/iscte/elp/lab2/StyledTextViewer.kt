package pt.iscte.elp.lab2

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.awt.Color
import java.io.File
import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.JTextPane
import javax.swing.SwingUtilities
import javax.swing.text.Style
import javax.swing.text.StyleConstants


class StyledTextViewer {
    private val textPane = JTextPane().apply {
        isEditable = false
    }

    private val frame = JFrame().apply {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setSize(400, 250)
        add(JScrollPane(textPane))
    }

    private val colors = mutableMapOf<Int, Style>()

    private val boldStyle = textPane.addStyle("bold", null).apply {
        StyleConstants.setBold(this, true)
    }

    val text: String get() = textPane.text

    fun setColor(start: Int, end: Int, color: Color) {
        SwingUtilities.invokeLater {
            if (!colors.contains(color.hashCode()))
                colors[color.hashCode()] =
                    textPane.addStyle(color.hashCode().toString(), null).apply {
                        StyleConstants.setForeground(this, color)
                    }

            textPane.styledDocument.setCharacterAttributes(
                start,
                end - start + 1,
                colors[color.hashCode()],
                true
            )
        }
    }

    fun setBold(start: Int, end: Int) {
        SwingUtilities.invokeLater {
            textPane.styledDocument.setCharacterAttributes(
                start,
                end - start + 1,
                boldStyle,
                true
            )
        }
    }


    fun open(text: String) {
        SwingUtilities.invokeLater {
            textPane.text = text
            frame.isVisible = true
        }
    }

    fun open(file: File) {
        open(
            try {
                file.readText()
            } catch (_: Exception) {
                ""
            }
        )
    }
}

fun main() {
    // demo
    val viewer = StyledTextViewer()
    viewer.open("""
        class Test {
        
        }
    """.trimIndent())
    viewer.setColor(0,4,Color.BLUE)
    viewer.setBold(6, 10)
}
