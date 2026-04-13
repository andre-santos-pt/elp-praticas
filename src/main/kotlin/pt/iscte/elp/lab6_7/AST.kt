package pt.iscte.elp.lab6_7

data class CompilationError(val message: String, val line: Int)

data class SourceRange(val start: Int, val stop: Int, val line: Int)

interface ASTNode {
    val range: SourceRange?
}

data class Script(
    val instructions: List<Instruction>,
    val parameters: List<String> = emptyList(),
    override val range: SourceRange? = null
) : ASTNode {
    val isValid: Boolean get() = validate().isEmpty()


    fun validate(): List<CompilationError> {
        val errors = mutableListOf<CompilationError>()

        fun checkVarDeclarations() {
            val declarations = mutableSetOf<String>()
            instructions.accept { inst, _ ->
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
        checkVarDeclarations()

        fun checkBreaks() {
            val scope = mutableListOf<ControlStructure>()
            instructions.accept { inst, context ->
                if (inst is ControlStructure) {
                    if (context.isBlockStart)
                        scope.add(inst)
                    else if (context.isBlockEnd)
                        scope.removeLast()
                } else if (inst is Break && scope.none { it is While })
                    errors.add(
                        CompilationError(
                            "break must be used within the scope of a while loop",
                            inst.range?.line ?: -1
                        )
                    )
            }
        }
        checkBreaks()
        return errors
    }
}


sealed interface Instruction : ASTNode {
    val varDependencies: List<String>
}

data class Assign(
    val id: String, val expression: Expression,
    override val range: SourceRange? = null
) : Instruction {
    override val varDependencies: List<String>
        get() = expression.varDependencies
}

data class Print(
    val expression: Expression,
    override val range: SourceRange? = null
) : Instruction {
    override val varDependencies: List<String>
        get() = expression.varDependencies
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
) : ControlStructure

interface LoopInstruction : Instruction {
    override val varDependencies: List<String>
        get() = emptyList()
}

class Break(
    override val range: SourceRange?
) : LoopInstruction

data class IfElse(
    override val guard: Expression,
    override val sequence: List<Instruction>,
    val alternative: List<Instruction>? = null,
    override val range: SourceRange? = null
) : ControlStructure

sealed interface Expression : ASTNode {
    val varDependencies: List<String>
}

data class Literal(
    val value: Int,
    override val range: SourceRange? = null
) : Expression {
    override val varDependencies: List<String>
        get() = emptyList()
}

data class Variable(
    val id: String,
    override val range: SourceRange? = null
) : Expression {
    override val varDependencies: List<String>
        get() = listOf(id)
}

data class BinaryExpression(
    val left: Expression,
    val operator: Operator,
    val right: Expression,
    override val range: SourceRange? = null
) : Expression {
    override val varDependencies: List<String>
        get() = left.varDependencies + right.varDependencies
}

enum class Operator {
    PLUS, MINUS, TIMES, DIV, MOD, EQUAL, NOTEQUAL, SMALLER, GREATER;
}



enum class Context {
    IF_START, WHILE_START, ELSE_START, BLOCK_END, NONE;

    val isBlockStart get() = this == IF_START || this == WHILE_START || this == ELSE_START

    val isBlockEnd get() = this == BLOCK_END
}

// Visitor (padrão de desenho)
fun List<Instruction>.accept(visitor: (Instruction, Context) -> Unit): Unit =
    forEach {
        if (it is IfElse) {
            visitor(it, Context.IF_START)
            it.sequence.accept(visitor)
            visitor(it, Context.BLOCK_END)
            if (it.alternative != null) {
                visitor(it, Context.ELSE_START)
                it.alternative.accept(visitor)
                visitor(it, Context.BLOCK_END)
            }
        }
        else if (it is While) {
            visitor(it, Context.WHILE_START)
            it.sequence.accept(visitor)
            visitor(it, Context.BLOCK_END)
        } else
            visitor(it, Context.NONE)
    }

