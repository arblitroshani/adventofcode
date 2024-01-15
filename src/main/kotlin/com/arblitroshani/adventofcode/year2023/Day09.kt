package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.AocPuzzle

typealias Input23d09 = List<List<Int>>

class Day09: AocPuzzle<Input23d09>() {

    override fun parseInput(lines: List<String>) =
        lines.map { it.split(" ").map(String::toInt) }

    override fun partOne(): Int =
        input.sumOf(::resolve)

    override fun partTwo(): Int =
        input.sumOf { resolve(it.reversed()) }

    private fun resolve(numbers: List<Int>): Int {
        var values = numbers
        var next = 0
        while (!values.all { it == 0 }) {
            next += values.last()
            values = values.windowed(2).map { it[1] - it[0] }
        }
        return next
    }
}

fun main() = Day09().solve(
    expectedAnswerForSampleInP1 = 114,
    expectedAnswerForSampleInP2 = 2,
)
