package pt.iscte.elp.lab6_7

import org.antlr.v4.runtime.ParserRuleContext
import pt.iscte.elp.lab6_7.SintraGrammarParser.*

fun ParserRuleContext.toRange() = SourceRange(this.start.startIndex, this.stop.stopIndex, this.start.line)

fun ScriptContext.toAST(): Script =
    Script(sequence.map { it.toAST() }, param().map { it.id.text }, this.toRange())

fun InstructionContext.toAST(): Instruction =
    if (assign() != null)
        assign().toAST()
    else if (print() != null)
        print().toAST()
    else if (if_() != null)
        if_().toAST()
    else if (while_() != null)
        while_().toAST()
    else if (break_() != null)
        break_().toAST()
    else
        throw IllegalArgumentException("Unknown statement: ${this.text}")

fun AssignContext.toAST(): Assign =
    Assign(ID().text, expression().toAST(), this.toRange())

fun PrintContext.toAST(): Print =
    Print(expression().toAST(), this.toRange())

fun IfContext.toAST(): IfElse =
    IfElse(
        expression().toAST(),
        sequence.map { it.toAST() },
        if(ELSE() == null) null else alternative.map { it.toAST() },
        this.toRange()
    )

fun WhileContext.toAST(): While =
    While(guard.toAST(), sequence.map { it.toAST() }, this.toRange())

fun BreakContext.toAST(): Break =
    Break(this.toRange())

fun String.toOperator(): Operator =
    when (this) {
        "+" -> Operator.PLUS
        "-" -> Operator.MINUS
        "*" -> Operator.TIMES
        "/" -> Operator.DIV
        "%" -> Operator.MOD
        "=" -> Operator.EQUAL
        "<>" -> Operator.NOTEQUAL
        "<" -> Operator.SMALLER
        ">" -> Operator.GREATER
        else -> throw IllegalArgumentException("Unknown operator $this")
    }

fun ExpressionContext.toAST(): Expression =
    if (inner != null)
        inner.toAST()
    else if (id != null)
        Variable(id.text, this.toRange())
    else if (value != null)
        Literal(value.text.toInt(), this.toRange())
    else BinaryExpression(
        left.toAST(),
        operator.text.toOperator(),
        right.toAST(),
        this.toRange()
    )