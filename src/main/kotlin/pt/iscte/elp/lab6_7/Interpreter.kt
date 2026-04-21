package pt.iscte.elp.lab6_7


class DivByZero(val line: Int) :
    RuntimeException("Division by zero on line $line")

class Interpreter(val script: Script) {

    val memory = mutableMapOf<String, Int>()

    fun run(vararg args: Pair<String, Int>) {
        if(!script.isValid)
            throw RuntimeException("script has compilation errors: ${script.validate()}")
        memory.clear()
        args.forEach { a ->
            memory[a.first] = a.second
        }
        script.instructions.execute()
    }

    fun List<Instruction>.execute(): LoopInstruction? {
        val it = iterator()
        while (it.hasNext()) {
            it.next().execute()?.let {
                return it
            }
        }
        return null
    }

    fun Instruction.execute(): LoopInstruction? {
        return when (this) {
            is Assign -> {
                memory[id] = expression.evaluate()
                null
            }

            is Print -> {
                println(expression.evaluate().toString())
                null
            }

            is IfElse -> {
                if (guard.evaluate() != 0)
                    sequence.execute()
                else
                    alternative?.execute()
            }

            is While -> {
                while (guard.evaluate() != 0) {
                    val loopInst = sequence.execute()
                    if (loopInst is Break)
                        break
                }
                null
            }

            else -> this as LoopInstruction
        }
    }

    private fun Expression.evaluate(): Int =
        when (this) {
            is Literal -> value
            is Variable -> memory[id]
                ?: throw RuntimeException("cannot find symbol $id")

            is BinaryExpression -> operator.calculate(left, right)
        }

    private fun Boolean.toInt() = if (this) 1 else 0

    private fun Operator.calculate(left: Expression, right: Expression): Int {
        val leftVal = left.evaluate()
        val rightVal = right.evaluate()
        return when (this) {
            Operator.PLUS -> leftVal + rightVal
            Operator.MINUS -> leftVal - rightVal
            Operator.TIMES -> leftVal * rightVal
            Operator.DIV -> if (rightVal == 0)
                throw DivByZero(right.range?.line ?: -1)
            else
                leftVal / rightVal

            Operator.MOD -> leftVal % rightVal
            Operator.EQUAL -> (leftVal == rightVal).toInt()
            Operator.NOTEQUAL -> (leftVal != rightVal).toInt()
            Operator.SMALLER -> (leftVal < rightVal).toInt()
            Operator.SMALLER_EQ -> (leftVal <= rightVal).toInt()
            Operator.GREATER -> (leftVal > rightVal).toInt()
            Operator.GREATER_EQ -> (leftVal >= rightVal).toInt()
        }
    }
}
