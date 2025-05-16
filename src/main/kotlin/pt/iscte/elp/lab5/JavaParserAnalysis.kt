package pt.iscte.elp.lab5

import com.github.javaparser.ast.Modifier
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.expr.*
import com.github.javaparser.ast.stmt.IfStmt
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import kotlin.jvm.optionals.getOrNull

data class Warning(val type: String, val line: Int, val problem: String) {
    override fun toString(): String = "Class $type, Line $line: $problem"
}

fun String.isUpperCase() = this.uppercase() == this

fun checkNameConventions(clazz: ClassOrInterfaceDeclaration): List<Warning> {
    val warnings = mutableListOf<Warning>()
    if (!clazz.nameAsString[0].isUpperCase())
        warnings.add(
            Warning(
                clazz.nameAsString,
                clazz.begin.get().line,
                "Class name (${clazz.nameAsString}) should start with an uppercase letter"
            )
        )

    clazz.fields.forEach { f ->
        if (f.modifiers.contains(Modifier.staticModifier()) &&
            f.modifiers.contains(Modifier.finalModifier())
        ) {
            if (f.variables.any { !it.nameAsString.isUpperCase() })
                warnings.add(
                    Warning(
                        clazz.nameAsString,
                        f.begin.get().line,
                        "Constant field names should be all uppercase (${f.variables.joinToString { it.nameAsString }})"
                    )
                )
        } else {
            if (f.variables.any { !it.nameAsString[0].isLowerCase() })
                warnings.add(
                    Warning(
                        clazz.nameAsString,
                        f.begin.get().line,
                        "Field name (${f.variables[0].nameAsString}) should start with a lowercase letter"
                    )
                )
        }
    }
    clazz.methods.forEach { m ->
        if (!m.nameAsString[0].isLowerCase())
            warnings.add(
                Warning(
                    clazz.nameAsString,
                    m.begin.get().line,
                    "Method name (${m.nameAsString}) should start with a lowercase letter"
                )
            )
        m.parameters.forEach {
            if (it.nameAsString[0].isUpperCase())
                warnings.add(
                    Warning(
                        clazz.nameAsString,
                        m.begin.get().line,
                        "Parameter name (${it.nameAsString}) should start with a lowercase letter"
                    )
                )
        }
    }
    return warnings
}

fun checkQuality(clazz: ClassOrInterfaceDeclaration): List<Warning> {
    val warnings = mutableListOf<Warning>()
    clazz.methods.forEach { m ->
        if (m.body.isPresent) {
            val body = m.body.get()
            body.accept(object : VoidVisitorAdapter<Any?>() {
                override fun visit(a: AssignExpr, arg: Any?) {
                    if (a.value is NameExpr && a.target == a.value)
                        warnings.add(
                            Warning(
                                clazz.nameAsString,
                                a.begin.get().line,
                                "Assignment to itself (${a})"
                            )
                        )
                }

                override fun visit(e: BinaryExpr, arg: Any?) {
                    if (e.operator == BinaryExpr.Operator.EQUALS && (
                                e.left == BooleanLiteralExpr(true) ||
                                        e.right == BooleanLiteralExpr(true)
                                )
                    )
                        warnings.add(
                            Warning(
                                clazz.nameAsString,
                                e.begin.get().line,
                                "Useless comparison with true (${e})"
                            )
                        )
                }

                override fun visit(iff: IfStmt, arg: Any?) {
                    super.visit(iff, arg)
                    if (iff.thenStmt.isEmptyStmt)
                        warnings.add(
                            Warning(
                                clazz.nameAsString,
                                iff.begin.get().line,
                                "Empty if block"
                            )
                        )

                    if (iff.thenStmt.isBlockStmt && iff.thenStmt.asBlockStmt().isEmpty)
                        warnings.add(
                            Warning(
                                clazz.nameAsString,
                                iff.begin.get().line,
                                "Empty if block"
                            )
                        )

                    if (iff.elseStmt.isPresent && iff.elseStmt.get().isBlockStmt && iff.elseStmt.get()
                            .asBlockStmt().isEmpty
                    )
                        warnings.add(
                            Warning(
                                clazz.nameAsString,
                                iff.elseStmt.get().begin.get().line,
                                "Empty else block"
                            )
                        )

                    if (iff.thenStmt.isReturnStmt && iff.thenStmt.asReturnStmt().expression.getOrNull() == BooleanLiteralExpr(
                            true
                        ) &&
                        iff.elseStmt.isPresent && iff.elseStmt.get().isReturnStmt && iff.elseStmt.get()
                            .asReturnStmt().expression.getOrNull() == BooleanLiteralExpr(false)
                    )
                        warnings.add(
                            Warning(
                                clazz.nameAsString,
                                iff.begin.get().line,
                                "If-else statement can be simplified to: return ${iff.condition}"
                            )
                        )
                }
            }, null)
        }
    }
    return warnings
}
