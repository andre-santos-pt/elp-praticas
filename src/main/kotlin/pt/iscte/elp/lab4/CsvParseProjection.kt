package pt.iscte.elp.lab4

import pt.iscte.elp.lab3.CsvGrammarParser

fun CsvGrammarParser.CsvContext.toAst(): CSV =
    CSV(this.line().map {
        it.toAst()
    })

fun CsvGrammarParser.LineContext.toAst() : Line =
    Line(this.element().map {
        if(it.BOOLEAN() != null)
            Bool(it.BOOLEAN().text == "true")
        else if(it.NUMBER() != null) {
            val text = it.NUMBER().text
            Numeric(try {
                text.toInt()
            }
            catch(e: NumberFormatException) {
                text.toDouble()
            })
        }
        else if(it.STRING() != null)
            Text(it.STRING().text.trim('"'))
        else
            None
    })
