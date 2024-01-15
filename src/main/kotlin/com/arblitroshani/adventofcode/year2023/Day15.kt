package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.AocPuzzle

private typealias Input23d15 = List<String>

private class Day15: AocPuzzle<Input23d15>() {

    override fun parseInput(lines: List<String>): Input23d15 =
        lines.first().split(",")

    override fun partOne(): Int =
        input.sumOf(::hash)

    override fun partTwo(): Int {
        data class Element(val name: String, var focalLength: Int)

        val slots = Array(256) { mutableListOf<Element>() }

        input.map { line ->
            val label: String
            var v: Int? = null

            if (line.endsWith('-'))
                label = line.dropLast(1)
            else {
                val parts = line.split("=")
                label = parts.first()
                v = parts.last().toInt()
            }

            val slot = slots[hash(label)]

            if (v == null) {
                slot.removeIf { it.name == label }
            } else if (slot.any { it.name == label}) {
                val index = slot.indexOfFirst { it.name == label }
                slot[index].focalLength = v
            } else {
                slot.add(Element(label, v))
            }
        }

        return slots.withIndex().sumOf { (i, slot) ->
            slot.withIndex().sumOf { (j, lens) ->
                (i + 1) * (j + 1) * lens.focalLength
            }
        }
    }

    fun hash(s: String): Int {
        var cv = 0
        s.forEach {
            cv += it.code
            cv *= 17
            cv %= 256
        }
        return cv
    }
}

fun main() = Day15().solve(
    expectedAnswerForSampleInP1 = 1320,
    expectedAnswerForSampleInP2 = 145,
)
