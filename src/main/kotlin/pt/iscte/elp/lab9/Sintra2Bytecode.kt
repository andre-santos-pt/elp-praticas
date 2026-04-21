package pt.iscte.elp.lab9

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.AALOAD
import org.objectweb.asm.Opcodes.ALOAD
import org.objectweb.asm.Opcodes.INVOKESTATIC
import org.objectweb.asm.Opcodes.ISTORE
import org.objectweb.asm.Opcodes.SIPUSH
import pt.iscte.elp.lab6_7.*
import pt.iscte.elp.lab8.ByteArrayClassLoader
import java.io.File
import java.io.FileOutputStream

class Sintra2Bytecode(val script: Script) {

    constructor(src: String) : this(sintraAST(src))

    lateinit var mv: MethodVisitor

    private val varTable = mutableListOf<String>()

    fun generate(className: String): ClassWriter {
        val cw = ClassWriter(ClassWriter.COMPUTE_FRAMES)
        cw.visit(
            Opcodes.V1_8,
            Opcodes.ACC_PUBLIC,
            className,
            null,
            "java/lang/Object",
            null
        )

        mv = cw.visitMethod(
            Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
            "main",
            "([Ljava/lang/String;)V",
            null,
            null
        )
        mv.visitCode()

        handleParameters()

        handle(script.instructions)

        mv.visitMaxs(0, 0)
        mv.visitInsn(Opcodes.RETURN)
        mv.visitEnd()

        cw.visitEnd()
        return cw
    }

    private fun handleParameters() {
        script.parameters.forEachIndexed { index, param ->
            mv.visitVarInsn(ALOAD, 0)
            mv.visitIntInsn(SIPUSH, index)
            mv.visitInsn(AALOAD)
            mv.visitMethodInsn(
                INVOKESTATIC,
                "java/lang/Integer",
                "parseInt",
                "(Ljava/lang/String;)I",
                false
            )
            mv.visitVarInsn(ISTORE, index + 1) // args/main é 0
            varTable.add(param)
        }
    }

    private fun varIndex(id: String) = varTable.indexOf(id).let {
        if(it == -1)
            it
        else it + 1 // args/main é 0
    }

    fun getClassFile(className: String): Class<*> {
        return ByteArrayClassLoader().define(className, generate(className).toByteArray())
    }

    fun writeClassFile(className: String) {
        val outputStream = FileOutputStream(File("$className.class"))
        outputStream.write(generate(className).toByteArray())
        outputStream.close()
    }

    fun handle(sequence: List<Instruction>) {
        sequence.forEach {
            when (it) {
                is Assign -> handleAssign(it)
                is Print -> handlePrint(it)
                is IfElse -> handleIf(it)
                is While -> handleWhile(it)
                is Break -> handleBreak()
            }
        }
    }

    private fun handlePrint(p: Print) {
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
        eval(p.expression)
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false)
    }

    private fun handleAssign(a: Assign) {
        var varIndex = varIndex(a.id)
        if (varIndex == -1)
            varTable.add(a.id)
        varIndex = varIndex(a.id)
        eval(a.expression)
        mv.visitVarInsn(ISTORE, varIndex)
    }

    private fun handleIf(iff: IfElse) {
        val elze = Label()
        val end = Label()
        eval(iff.guard)
        mv.visitJumpInsn(Opcodes.IFEQ, elze)
        handle(iff.sequence)
        mv.visitJumpInsn(Opcodes.GOTO, end)
        mv.visitLabel(elze)
        iff.alternative?.let {
            handle(it)
        }
        mv.visitLabel(end)
    }

    val whileContext = mutableListOf<Label>()

    private fun handleWhile(w: While) {
        val startLoop = Label()
        val endLoop = Label()
        whileContext.add(endLoop)
        mv.visitLabel(startLoop)
        eval(w.guard)
        mv.visitJumpInsn(Opcodes.IFEQ, endLoop)
        handle(w.sequence)
        mv.visitJumpInsn(Opcodes.GOTO, startLoop)
        mv.visitLabel(endLoop)
        whileContext.removeLast()
    }

    private fun handleBreak() {
        if(whileContext.isEmpty())
            throw RuntimeException("break not in context of while")

        mv.visitJumpInsn(Opcodes.GOTO, whileContext.last())
    }


    private val Operator.jvmOp: Int
        get() = when (this) {
            Operator.PLUS -> Opcodes.IADD
            Operator.MINUS -> Opcodes.ISUB
            Operator.TIMES -> Opcodes.IMUL
            Operator.DIV -> Opcodes.IDIV
            Operator.MOD -> Opcodes.IREM

            Operator.EQUAL -> Opcodes.IF_ICMPEQ
            Operator.NOTEQUAL -> Opcodes.IF_ICMPNE

            Operator.SMALLER -> Opcodes.IF_ICMPLT
            Operator.SMALLER_EQ -> Opcodes.IF_ICMPLE
            Operator.GREATER -> Opcodes.IF_ICMPGT
            Operator.GREATER_EQ -> Opcodes.IF_ICMPGE
        }

    private fun eval(e: Expression) {
        when (e) {
            is Literal -> {
                mv.visitIntInsn(SIPUSH, e.value)
            }

            is Variable -> {
                val varIndex = varIndex(e.id)
                if (varIndex == -1)
                    throw RuntimeException("var not initialized ${e.id}")

                mv.visitVarInsn(Opcodes.ILOAD, varIndex)
            }

            is BinaryExpression -> {
                eval(e.left)
                eval(e.right)
                if (e.operator.isArithmetic)
                    mv.visitInsn(e.operator.jvmOp)
                else if (e.operator.isRelational) {
                    val trueLabel = Label()
                    val falseLabel = Label()
                    mv.visitJumpInsn(e.operator.jvmOp, trueLabel)
                    mv.visitIntInsn(Opcodes.BIPUSH, 0)
                    mv.visitJumpInsn(Opcodes.GOTO, falseLabel)
                    mv.visitLabel(trueLabel)
                    mv.visitIntInsn(Opcodes.BIPUSH, 1)
                    mv.visitLabel(falseLabel)
                }
                else throw RuntimeException("unsupported $e")
            }
        }
    }
}
