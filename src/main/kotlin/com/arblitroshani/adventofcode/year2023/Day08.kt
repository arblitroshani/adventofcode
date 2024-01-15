package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.AocPuzzle
import com.arblitroshani.adventofcode.util.lcm

data class Input23d08(
    val instructions: List<Boolean>,
    val elements: Map<String, String>,
)

class Day08: AocPuzzle<Input23d08>() {

    override fun parseInput(lines: List<String>): Input23d08 {
        val elements = mutableMapOf<String, String>()
        lines.drop(2).forEach { line ->
            val (name, l, r) = line.split(" = (", ", ", ")")
            elements["${name}l"] = l
            elements["${name}r"] = r
        }
        return Input23d08(
            instructions = lines[0].map { it == 'L' },
            elements = elements,
        )
    }

    override fun partOne(): Long =
        stepsForPart1("AAA") { it.endsWith("ZZZ") }

    override fun partTwo(): Long =
        input.elements.keys
            .asSequence()
            .map { it.dropLast(1) }
            .filter { it.endsWith('A') }
            .distinct()
            .map { el -> stepsForPart1(el) { it.endsWith('Z') } }
            .fold(1, ::lcm)

    private fun stepsForPart1(
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
}

fun main() = Day08().solve(
    expectedAnswerForSampleInP1 = 2L,
)
