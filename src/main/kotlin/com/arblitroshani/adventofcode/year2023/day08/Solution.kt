package com.arblitroshani.adventofcode.year2023.day08

import com.arblitroshani.adventofcode.framework.solution
import com.arblitroshani.adventofcode.util.lcm

private data class Input(
    val instructions: List<Boolean>,
    val elements: Map<String, String>,
)

fun main() = solution<Input>(2023, 8) {

    fun stepsForPart1(
        input: Input,
        currentElement: String,
        isComplete: (String) -> Boolean,
    ): Long {
        var currentEl = currentElement
        var steps = 0L
        while (!isComplete(currentEl)) {
            val index = steps % (input.instructions.size).toLong()
            val instruction = input.instructions[index.toInt()]
            currentEl = input.elements[currentEl + if (instruction) 'l' else 'r'] ?: throw Exception()
            steps += 1
        }
        return steps
    }

    parseInput { lines ->
        val elements = mutableMapOf<String, String>()
        lines.drop(2).forEach { line ->
            val (name, l, r) = line.split(" = (", ", ", ")")
            elements["${name}l"] = l
            elements["${name}r"] = r
        }
        Input(
            instructions = lines[0].map { it == 'L' },
            elements = elements,
        )
    }

    partOne { input ->
        stepsForPart1(input, "AAA") { it.endsWith("ZZZ") }
    }

    partTwo { input ->
        input.elements.keys
            .asSequence()
            .map { it.dropLast(1) }
            .filter { it.endsWith('A') }
            .distinct()
            .map { el -> stepsForPart1(input, el) { it.endsWith('Z') } }
            .fold(1, ::lcm)
    }

    partOneTest {
        """
            RL

            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent() shouldOutput 2

        """
            LLR

            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent() shouldOutput 6
    }

    partTwoTest {
        """
            LR

            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)
        """.trimIndent() shouldOutput 6
    }
}
