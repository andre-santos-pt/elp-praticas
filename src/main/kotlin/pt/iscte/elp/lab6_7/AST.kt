package pt.iscte.elp.lab6_7


val TAB = "    "

fun List<Instruction>.toCode(indent: Int = 0) =
    joinToString("\n", postfix = "\n") { it.toCode(indent) }

data class CompilationError(val message: String, val line: Int)

data class SourceRange(val start: Int, val stop: Int, val line: Int)

interface ASTNode {
    val range: SourceRange?
}

data class Script(
    val instructions: List<Instruction>,
    val parameters: List<String> = emptyList(),
    override val range: SourceRange? = null
): ASTNode {
    val isValid: Boolean get() = validate().isEmpty()


    fun validate(): List<CompilationError> {
        val errors = mutableListOf<CompilationError>()

        fun List<Instruction>.checkVarDeclarations(declarations: MutableSet<String> = mutableSetOf()) {
            forEach { inst ->
                inst.varDependencies
                    .filter { it !in parameters && it !in declarations }
                    .forEach {
                        errors.add(
                            CompilationError(
                                "variable not found: $it",
                                inst.range?.line ?: -1
                            )
                        )
                    }

                if (inst is Assign)
                    declarations.add(inst.id)
            }
        }
        instructions.checkVarDeclarations()

        fun List<Instruction>.checkBreaks(scope: MutableList<ControlStructure> = mutableListOf()) {
            forEach { inst ->
                if (inst is ControlStructure) {
                    scope.add(inst)
                    inst.sequence.checkBreaks(scope)
                    scope.removeLast()
                }
                else if(inst is Break && scope.none { it is While })
                    errors.add(CompilationError("break must be used within the scope of a while loop", inst.range?.line ?: -1))
            }
        }
        instructions.checkBreaks()
        return errors
    }


    fun toCode() =
        parameters.joinToString(postfix = "\n") { "param $it" } + instructions.toCode()
}


sealed interface Instruction: ASTNode {
    val varDependencies: List<String>

    fun toCode(indent: Int) = TAB.repeat(indent) + toString()
}

data class Assign(
    val id: String, val expression: Expression,
    override val range: SourceRange? = null
) : Instruction {
    override val varDependencies: List<String>
        get() = expression.varDependencies

    override fun toString(): String = "$id := $expression"
}

data class Print(
    val expression: Expression,
    override val range: SourceRange? = null
) : Instruction {
    override val varDependencies: List<String>
        get() = expression.varDependencies

    override fun toString(): String = "print $expression"
}


sealed interface ControlStructure : Instruction {
    val guard: Expression
    val sequence: List<Instruction>

    override val varDependencies: List<String>
        get() = guard.varDependencies
}


data class While(
    override val guard: Expression,
    override val sequence: List<Instruction>,
    override val range: SourceRange? = null
) : ControlStructure {
    override fun toCode(indent: Int): String =
        TAB.repeat(indent) + "while $guard {\n" + sequence.toCode(indent + 1) + TAB.repeat(
            indent
        ) + "}"

}

interface LoopInstruction : Instruction {
    override val varDependencies: List<String>
        get() = emptyList()
}

class Break(
    override val range: SourceRange?
) : LoopInstruction {
    override fun toString(): String = "break"
}

data class IfElse(
    override val guard: Expression,
    override val sequence: List<Instruction>,
    val alternative: List<Instruction>? = null,
    override val range: SourceRange? = null
) : ControlStructure {
    override fun toCode(indent: Int): String =
        TAB.repeat(indent) + "if $guard {\n" + sequence.toCode(indent + 1) +
                TAB.repeat(indent) + "}" +
                (alternative?.let {
                    "\n" + TAB.repeat(indent) + "else {\n" + it.toCode(
                        indent + 1
                    ) + TAB.repeat(indent) + "}"
                } ?: "")
}


sealed interface Expression : ASTNode{
    val varDependencies: List<String>
    //val range: SourceRange?
}

data class Literal(
    val value: Int,
    override val range: SourceRange? = null
) : Expression {
    override val varDependencies: List<String>
        get() = emptyList()

    override fun toString(): String = "$value"
}

data class Variable(
    val id: String,
    override val range: SourceRange? = null
) : Expression {
    override val varDependencies: List<String>
        get() = listOf(id)

    override fun toString(): String = id
}

data class BinaryExpression(
    val left: Expression,
    val operator: Operator,
    val right: Expression,
    override val range: SourceRange? = null
) : Expression {
    override val varDependencies: List<String>
        get() = left.varDependencies + right.varDependencies

    override fun toString(): String = "$left $operator $right"
}

enum class Operator {
    PLUS, MINUS, TIMES, DIV, MOD, EQUAL, NOTEQUAL, SMALLER, GREATER;

    override fun toString(): String =
        when (this) {
            PLUS -> "+"
            MINUS -> "-"
            TIMES -> "*"
            DIV -> "/"
            MOD -> "%"
            EQUAL -> "="
            NOTEQUAL -> "<>"
            SMALLER -> "<"
            GREATER -> ">"
        }
}

