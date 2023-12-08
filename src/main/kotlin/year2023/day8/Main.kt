package year2023.day8

import util.InputReader
import util.lcm

fun main() {
    val ip = InputReader(2023, 8).read()

    val instructions = ip[0].map { it == 'L' }
    val elements = mutableMapOf<String, String>()
    ip.drop(2).forEach { line ->
        val (name, l, r) = line.split(" = (", ", ", ")")
        elements["${name}l"] = l
        elements["${name}r"] = r
    }

    println("Solution pt1: ${stepsForPart1(instructions, elements)}")
    println("Solution pt2: ${stepsForPart2(instructions, elements)}")
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
        .filter { it.endsWith("Al") || it.endsWith("Ar") }
        .map { el -> stepsForPart1(instructions, elements, el.dropLast(1)) { it.endsWith('Z') } }
        .fold(1, ::lcm)
