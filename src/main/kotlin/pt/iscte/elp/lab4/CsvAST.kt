package pt.iscte.elp.lab4

data class CSV(val lines: List<Line>) {
    fun prettyPrint(): String {
        val builder = StringBuilder()
        lines.forEach { l ->
            val line = l.values.joinToString(", ") {
                when (it) {
                    None -> " "
                    is Bool -> it.value.toString()
                    is Numeric -> it.value.toString()
                    is Text -> "\"${it.value}\""
                }
            } // ..., ..., ...
            builder.append(line).append(System.lineSeparator())
        }
        return builder.toString()
    }

    fun isWellFormed() =
        lines.none { it.values.isEmpty() } &&
                lines.map { it.values.size }.distinct().size == 1


    fun isWellTyped() =
        (0 until lines.first().columns).all { col ->
            (0 until lines.size).map { line ->
                lines[line].values[col]
            }.filter { it !is None }.size == 1
        }
}

data class Line(val values: List<Value>) {
    val columns: Int get() = values.size
}

sealed interface Value

data object None : Value

data class Text(val value: String) : Value

data class Numeric(val value: Number) : Value

data class Bool(val value: Boolean) : Value