package pt.iscte.elp.lab6_7

fun main() {
    val script = Script(
        listOf(
            Assign("s", Literal(0)),
            Assign("i", Literal(0)),
            While(
                BinaryExpression(Variable("i"), Operator.SMALLER, Literal(10)), listOf(
                    IfElse(
                        BinaryExpression(
                            BinaryExpression(Variable("i"), Operator.MOD, Literal(2)),
                            Operator.EQUAL, Literal(0)
                        ), listOf(
                            Assign("s", BinaryExpression(Variable("s"), Operator.PLUS, Variable("i"))),
                        )
                    ),
                    Assign("i", BinaryExpression(Variable("i"), Operator.PLUS, Literal(1)))
                )
            ),

            Print(Variable("s")) // 20
        )
    )
    println(script.toSrc())
    println(script.validate())
    val interpreter = Interpreter(script)
    interpreter.run()

}