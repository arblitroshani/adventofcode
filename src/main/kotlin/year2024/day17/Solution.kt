package year2024.day17

import framework.solution
import util.InputParsing
import util.pow
import kotlin.math.max
import kotlin.math.min

private data class Input(val ra: Long, val program: List<Int>)

fun main() = solution<Input>(2024, 17) {

    fun execute(input: Input, onOutput: (Long) -> (Boolean)) {
        val registers = mutableListOf(input.ra, 0, 0)
        var ptr = 0
        while (ptr < input.program.size) {
            val opcode = input.program[ptr++]
            val operand = input.program[ptr++]
            val combo: Long = when (operand) {
                in 0..3 -> operand.toLong()
                in 4..6 -> registers[operand - 4]
                else -> error("Should not appear in valid programs")
            }
            when (opcode) {
                0, 6, 7 -> registers[max(0, opcode - 5)] = registers[0] shr combo.toInt()
                1 -> registers[1] = registers[1] xor operand.toLong()
                2 -> registers[1] = combo % 8
                3 -> if (registers[0] != 0L) ptr = operand
                4 -> registers[1] = registers[1] xor registers[2]
                5 -> if (!onOutput(combo % 8)) break
            }
        }
    }

    parseInput { lines ->
        val (registerLines, programLine) = InputParsing.splitListByEmptyLines(lines)
            .map { group -> group.map { it.split(": ").drop(1).first() } }
        Input(
            ra = registerLines.first().toLong(),
            program = programLine.first().split(",").map(String::toInt),
        )
    }

    partOne { input ->
        val output = mutableListOf<Long>()
        execute(input, output::add)
        output.joinToString(",")
    }

    partTwo { input ->
        fun matchingOutputs(a: Long): Int {
            var outputSize = 0
            execute(input.copy(ra = a)) {
                if (outputSize >= input.program.size) return@execute false
                if (input.program[outputSize] != it.toInt()) return@execute false
                outputSize++
                return@execute true
            }
            return outputSize
        }

        // step 1: find first 10 numbers that have at least first 8 outputs same as expected
        // val matchesOfAtLeast8 = mutableListOf<Long>()
        // for (a in 0 until Long.MAX_VALUE) {
        //     if (matchingOutputs(a) >= 8) {
        //         matchesOfAtLeast8.add(a)
        //         if (matchesOfAtLeast8.size == 10) break
        //     }
        // }

        // step 2: analyze `diffs`: repeating pattern between (2^8) and (2^26 - 2^8)
        // val diffs = matchesOfAtLeast8.windowed(2)

        // step 3: find first match that fulfills condition
        var firstFullMatch = -1L
        val firstMatchOfAtLeast8 = 23948989L // = matchesOfAtLeast8.first()
        for (a in firstMatchOfAtLeast8 until Long.MAX_VALUE step (2L pow 26)) {
            if (matchingOutputs(a) == input.program.size) {
                firstFullMatch = a
                break
            }
        }

        // step 4: search for any value smaller than the firstFullMatch
        var smallestFullMatch = firstFullMatch
        val startTime = System.currentTimeMillis()
        val maxSearchDuration = 5000
        for (a in firstFullMatch downTo 0 step (2L pow 8)) {
            if (matchingOutputs(a) == input.program.size) smallestFullMatch = min(smallestFullMatch, a)
            if (System.currentTimeMillis() - startTime > maxSearchDuration) break
        }
        smallestFullMatch
    }

    partOneTest {
        """
            Register A: 2024
            Register B: 0
            Register C: 0

            Program: 0,1,5,4,3,0
        """.trimIndent() shouldOutput "4,2,5,6,7,7,7,7,3,1,0"

        """
            Register A: 117440
            Register B: 0
            Register C: 0

            Program: 0,3,5,4,3,0
        """.trimIndent() shouldOutput "0,3,5,4,3,0"
    }
}
