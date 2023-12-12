package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.util.InputReader
import com.arblitroshani.adventofcode.util.lcm
import com.arblitroshani.adventofcode.util.print

fun main() {
    val ip = InputReader(2023, 8).read()

    val instructions = ip[0].map { it == 'L' }
    val elements = mutableMapOf<String, String>()
    ip.drop(2).forEach { line ->
        val (name, l, r) = line.split(" = (", ", ", ")")
        elements["${name}l"] = l
        elements["${name}r"] = r
    }

    stepsForPart1(instructions, elements).print()
    stepsForPart2(instructions, elements).print()
}

fun stepsForPart1(
    instructions: List<Boolean>,
    elements: Map<String, String>,
    startElement: String = "AAA",
    isComplete: (String) -> Boolean = { it == "ZZZ" },
): Long {
    var currentEl = startElement
    var steps = 0L
    while (!isComplete(currentEl)) {
        val index = steps % (instructions.size).toLong()
        val instruction = instructions[index.toInt()]
        currentEl = elements[currentEl + if (instruction) 'l' else 'r'] ?: throw Exception()
        steps += 1
    }
    return steps
}

fun stepsForPart2(instructions: List<Boolean>, elements: Map<String, String>): Long =
    elements.keys
        .asSequence()
        .map { it.dropLast(1) }
        .filter { it.endsWith('A') }
        .distinct()
        .map { el -> stepsForPart1(instructions, elements, el) { it.endsWith('Z') } }
        .fold(1, ::lcm)
