package pt.iscte.elp.lab3

import pt.iscte.elp.lab3.CsvGrammarParser.*

fun isWellFormed(csv: CsvContext): Boolean {
    for(i in 0 until csv.line().size - 1)
        if(csv.line(i).value().isEmpty() || csv.line(i).value().size != csv.line(i+1).value().size)
            return false
    return true
}

fun CsvContext.isWellFormedExt() =
    this.line()
        .map { it.value().size }
        .toSet().size == 1


fun ValueContext.type() =
    if(NUMBER() != null) NUMBER().symbol.type
    else if(BOOLEAN() != null) BOOLEAN().symbol.type
    else if(STRING() != null) STRING().symbol.type
    else null

fun isWellTyped(csv: CsvContext) : Boolean {
    require(isWellFormed(csv))

    for(c in 0 until csv.line(0).value().size) {
        val typeSet = mutableSetOf(csv.line(0).value(c).type())
        for (l in 1 until csv.line().size)
            typeSet.add(csv.line(l).value(c).type())

        typeSet.remove(null)
        if(typeSet.size > 1)
            return false
    }
    return true
}