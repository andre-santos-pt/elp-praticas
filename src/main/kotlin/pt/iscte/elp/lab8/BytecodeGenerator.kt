package pt.iscte.elp.lab8

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes.*
import java.io.FileOutputStream

fun main() {
    val outputStream = FileOutputStream("Lab8.class")
    outputStream.write(generateClassBytes())
    outputStream.close()
}

fun generateClassBytes(): ByteArray {
    val cw = ClassWriter(ClassWriter.COMPUTE_FRAMES)
    cw.visit(V1_8, ACC_PUBLIC, "Lab8", null, "java/lang/Object", null)

    addIncMethod(cw)
    addIsPositiveMethod(cw)
    addAbsMethod(cw)
    addIsEvenMethod(cw)
    addFirstDigitMethod(cw)
    addFactorialMethod(cw)
    addFactorialRecMethod(cw)
    addSumIntervalMethod(cw)

    generateMainWithCallToInc(cw)

    cw.visitEnd()
    return cw.toByteArray()
}

fun generateMainWithCallToInc(cw: ClassWriter) {
    val mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null)
    mv.visitCode()

    // conversao de primeiro argumento de programa em inteiro
    mv.visitVarInsn(ALOAD, 0)
    mv.visitInsn(ICONST_0)
    mv.visitInsn(AALOAD)
    mv.visitMethodInsn(INVOKESTATIC,
        "java/lang/Integer",
        "parseInt",
        "(Ljava/lang/String;)I",
        false);

    // invocacao
    mv.visitMethodInsn(INVOKESTATIC, "Lab8", "inc", "(I)I", false)

    // argumento
    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")

    // troca ordem dos dois valores no topo da pilha
    mv.visitInsn(SWAP)

    // invocacao
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false)

    // retorno (sem resultado)
    mv.visitInsn(RETURN)
    mv.visitMaxs(0, 0)
    mv.visitEnd()
}

fun addIncMethod(cw: ClassWriter) {
    val mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "inc", "(I)I", null, null)
    mv.visitCode()

    mv.visitVarInsn(ILOAD, 0)
    mv.visitInsn(ICONST_1)
    mv.visitInsn(IADD)
    mv.visitInsn(IRETURN)

    mv.visitMaxs(0, 0)
    mv.visitEnd()
}

fun addIsPositiveMethod(cw: ClassWriter) {
    val mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "isPositive", "(I)Z", null, null)
    mv.visitCode()

    val end = Label()
    val trueCase = Label()

    mv.visitVarInsn(ILOAD, 0)
    mv.visitJumpInsn(IFGT, trueCase)
    mv.visitInsn(ICONST_0)
    mv.visitJumpInsn(GOTO, end)

    mv.visitLabel(trueCase)
    mv.visitInsn(ICONST_1)

    mv.visitLabel(end)
    mv.visitInsn(IRETURN)

    mv.visitMaxs(0, 0)
    mv.visitEnd()
}

fun addAbsMethod(cw: ClassWriter) {
    val mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "abs", "(I)I", null, null)
    mv.visitCode()

    mv.visitVarInsn(ILOAD, 0)
    val branch = Label()
    val exit = Label()

    mv.visitJumpInsn(IFGE, exit)
    mv.visitVarInsn(ILOAD, 0)
    mv.visitInsn(INEG)
    mv.visitJumpInsn(GOTO, branch)

    mv.visitLabel(exit)
    mv.visitVarInsn(ILOAD,0)

    mv.visitLabel(branch)
    mv.visitInsn(IRETURN)

    mv.visitMaxs(0, 0)
    mv.visitEnd()
}

fun addIsEvenMethod(cw: ClassWriter) {
    val mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "isEven", "(I)Z", null, null)
    mv.visitCode()

    mv.visitVarInsn(ILOAD, 0)
    mv.visitIntInsn(BIPUSH, 2)
    mv.visitInsn(IREM)

    val trueCase = Label()
    mv.visitJumpInsn(IFEQ, trueCase)
    mv.visitInsn(ICONST_0)
    mv.visitInsn(IRETURN)

    mv.visitLabel(trueCase)
    mv.visitInsn(ICONST_1)
    mv.visitInsn(IRETURN)

    mv.visitMaxs(0, 0)
    mv.visitEnd()
}


fun addFirstDigitMethod(cw: ClassWriter) {
    val mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "firstDigit", "(I)I", null, null)
    mv.visitCode()

    val end = Label()
    val loop = Label()

    mv.visitLabel(loop)
    mv.visitVarInsn(ILOAD, 0)
    mv.visitVarInsn(BIPUSH, 10)
    mv.visitJumpInsn(IF_ICMPLT, end)
    mv.visitVarInsn(ILOAD, 0)
    mv.visitIntInsn(BIPUSH, 10)
    mv.visitInsn(IDIV)
    mv.visitVarInsn(ISTORE, 0)
    mv.visitJumpInsn(GOTO, loop)

    mv.visitLabel(end)
    mv.visitVarInsn(ILOAD, 0)
    mv.visitInsn(IRETURN)

    mv.visitMaxs(0, 0)
    mv.visitEnd()
}



fun addFactorialMethod(cw: ClassWriter) {
    val mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "fact", "(I)I", null, null)
    mv.visitCode()

    mv.visitVarInsn(ILOAD, 0)
    mv.visitVarInsn(ISTORE, 1) // iter

    mv.visitInsn(ICONST_1)
    mv.visitVarInsn(ISTORE, 2) // prod

    val exit = Label()
    val loop = Label()

    mv.visitLabel(loop)
    mv.visitVarInsn(ILOAD,1)
    mv.visitJumpInsn(IFLE, exit)

    mv.visitVarInsn(ILOAD,2)
    mv.visitVarInsn(ILOAD,1)
    mv.visitInsn(IMUL)

    mv.visitVarInsn(ISTORE, 2)

    mv.visitVarInsn(ILOAD,1)
    mv.visitInsn(ICONST_1)
    mv.visitInsn(ISUB)
    mv.visitVarInsn(ISTORE, 1)
    mv.visitJumpInsn(GOTO, loop)

    mv.visitLabel(exit)
    mv.visitVarInsn(ILOAD,2)
    mv.visitInsn(IRETURN)
    mv.visitMaxs(0, 0)
    mv.visitEnd()
}

fun addFactorialRecMethod(cw: ClassWriter) {
    val mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "factRec", "(I)I", null, null)
    mv.visitCode()

    val base = Label()

    mv.visitVarInsn(ILOAD, 0)
    mv.visitInsn(ICONST_1)
    mv.visitJumpInsn(IF_ICMPLE, base)

    mv.visitVarInsn(ILOAD, 0)
    mv.visitInsn(ICONST_1)
    mv.visitInsn(ISUB)
    mv.visitMethodInsn(INVOKESTATIC, "Lab8", "factRec", "(I)I", false)
    mv.visitVarInsn(ILOAD, 0)
    mv.visitInsn(IMUL)
    mv.visitInsn(IRETURN)

    mv.visitLabel(base)
    mv.visitInsn(ICONST_1)
    mv.visitInsn(IRETURN)

    mv.visitMaxs(0, 0)
    mv.visitEnd()
}

fun addSumIntervalMethod(cw: ClassWriter) {
    val mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "sumInterval", "(II)I", null, null)
    mv.visitCode()

    // var indexes
    val min = 0
    val max = 1
    val it = 2
    val sum = 3

    val end = Label()
    val loop = Label()

    // it = min
    mv.visitVarInsn(ILOAD, min)
    mv.visitVarInsn(ISTORE, it)

    // sum = 0
    mv.visitInsn(ICONST_0)
    mv.visitVarInsn(ISTORE, sum)

    // while(it <= max)
    mv.visitLabel(loop)
    mv.visitVarInsn(ILOAD, it)
    mv.visitVarInsn(ILOAD, max)
    mv.visitJumpInsn(IF_ICMPGT, end)

    // sum+=it
    mv.visitVarInsn(ILOAD, sum)
    mv.visitVarInsn(ILOAD, it)
    mv.visitInsn(IADD)
    mv.visitVarInsn(ISTORE, sum)

    // it++
    mv.visitInsn(ICONST_1)
    mv.visitVarInsn(ILOAD, it)
    mv.visitInsn(IADD)
    mv.visitVarInsn(ISTORE, it)

    mv.visitJumpInsn(GOTO, loop)

    mv.visitLabel(end)
    mv.visitVarInsn(ILOAD, sum)
    mv.visitInsn(IRETURN)

    mv.visitMaxs(0, 0)
    mv.visitEnd()
}