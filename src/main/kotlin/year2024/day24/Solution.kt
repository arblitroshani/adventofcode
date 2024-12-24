package year2024.day24

import framework.solution
import util.InputParsing

private data class Op(val i1: String, val gate: String, val i2: String, val o: String)
private data class Input(val inputs: Map<String, Boolean>, val ops: List<Op>)

fun main() = solution<Input>(2024, 24) {

    parseInput { lines ->
        val (initialInputLines, gatesAndWireLines) = InputParsing.splitListByEmptyLines(lines)
        Input(
            inputs = initialInputLines.associate {
                val (k, v) = it.split(": ")
                k to (v.toInt() == 1)
            },
            ops = gatesAndWireLines.map {
                val (i1, gate, i2, o) = it.split(" -> ", " ")
                Op(i1, gate, i2, o)
            }
        )
    }

    partOne { (inputs, ops) ->
        val allInputs = inputs.toMutableMap()
        val remainingOps = ops.toMutableList()

        var index = -1
        while (remainingOps.isNotEmpty()) {
            if (++index >= remainingOps.size) index = 0
            val op = remainingOps[index]
            val i1 = allInputs[op.i1] ?: continue
            val i2 = allInputs[op.i2] ?: continue
            allInputs[op.o] = when (op.gate) {
                "AND" -> i1 && i2
                "OR" -> i1 || i2
                "XOR" -> i1 != i2
                else -> error("")
            }
            remainingOps.remove(op)
            index--
        }

        allInputs
            .filterKeys { it.startsWith('z') }
            .toList()
            .sortedByDescending { it.first }
            .map { if (it.second) 1 else 0 }
            .joinToString("")
            .toLong(2)
    }

    partTwo { (_, ops) ->
        val fixedOps = ops.toMutableList()
        val swappedOutputs = mutableListOf<String>()

        fun findOperation(i1: String, gate: String, i2: String): Op? =
            fixedOps
                .filter { it.gate == gate }
                .firstOrNull { (it.i1 == i1 && it.i2 == i2) || (it.i1 == i2 && it.i2 == i1) }

        fun findOperationOutput(i1: String, gate: String, i2: String): String =
            findOperation(i1, gate, i2)!!.o

        fun swap(op1: Op, op2: Op) {
            swappedOutputs.addAll(listOf(op1.o, op2.o))
            fixedOps.removeAll(listOf(op1, op2))
            fixedOps.addAll(listOf(op1.copy(o = op2.o), op2.copy(o = op1.o)))
        }

        (1 until 45)
            .map { if (it < 10) "0$it" else "$it" }
            .fold(findOperationOutput("x00", "AND", "y00")) { acc, i ->
                val sumOp = findOperation("x$i", "XOR", "y$i")!!
                var sum = sumOp.o

                val outputOperation = findOperation(sum, "XOR", acc)
                if (outputOperation == null) {
                    val outputOp = fixedOps.first { it.o == "z$i" }
                    val otherOp = fixedOps.first { it.o == outputOp.i2 }
                    sum = otherOp.o
                    swap(sumOp, otherOp)
                } else if (outputOperation.o != "z$i") {
                    swap(outputOperation, fixedOps.first { it.o == "z$i" })
                }

                val c1 = findOperationOutput("x$i", "AND", "y$i")
                val c2 = findOperationOutput(sum, "AND", acc)
                findOperationOutput(c1, "OR", c2) // carryOut
            }

        swappedOutputs.sorted().joinToString(",")
    }

    partOneTest {
        """
            x00: 1
            x01: 0
            x02: 1
            x03: 1
            x04: 0
            y00: 1
            y01: 1
            y02: 1
            y03: 1
            y04: 1

            ntg XOR fgs -> mjb
            y02 OR x01 -> tnw
            kwq OR kpj -> z05
            x00 OR x03 -> fst
            tgd XOR rvg -> z01
            vdt OR tnw -> bfw
            bfw AND frj -> z10
            ffh OR nrd -> bqk
            y00 AND y03 -> djm
            y03 OR y00 -> psh
            bqk OR frj -> z08
            tnw OR fst -> frj
            gnj AND tgd -> z11
            bfw XOR mjb -> z00
            x03 OR x00 -> vdt
            gnj AND wpb -> z02
            x04 AND y00 -> kjc
            djm OR pbm -> qhw
            nrd AND vdt -> hwm
            kjc AND fst -> rvg
            y04 OR y02 -> fgs
            y01 AND x02 -> pbm
            ntg OR kjc -> kwq
            psh XOR fgs -> tgd
            qhw XOR tgd -> z09
            pbm OR djm -> kpj
            x03 XOR y03 -> ffh
            x00 XOR y04 -> ntg
            bfw OR bqk -> z06
            nrd XOR fgs -> wpb
            frj XOR qhw -> z04
            bqk OR frj -> z07
            y03 OR x01 -> nrd
            hwm AND bqk -> z03
            tgd XOR rvg -> z12
            tnw OR pbm -> gnj
        """.trimIndent() shouldOutput 2024
    }
}
