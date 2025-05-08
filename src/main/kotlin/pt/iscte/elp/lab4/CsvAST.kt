package pt.iscte.elp.lab4

data class CSV(val lines: List<Line>) {
    fun prettyPrint(): String {
        val builder = StringBuilder()
        lines.forEach { l ->
            val line = l.values.joinToString {
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

    fun checkLinesEqualLength(): Boolean {
        val lengths = lines.map { it.values.size }
        return lengths.distinct().size <= 1
    }
}

data class Line(val values: List<Value>)

sealed interface Value

data object None : Value

data class Text(val value: String) : Value

data class Numeric(val value: Number) : Value

data class Bool(val value: Boolean) : Value