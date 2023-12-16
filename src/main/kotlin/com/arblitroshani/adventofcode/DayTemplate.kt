package com.arblitroshani.adventofcode

typealias Input23dX = MutableList<MutableList<Char>>

class DayX: AocPuzzle<Input23dX>() {

    override fun parseInput(lines: List<String>): Input23dX {
        return lines.map { it.toMutableList() }.toMutableList()
    }

    override fun partOne(): Int {
        return 0
    }

    override fun partTwo(): Int {
        return 0
    }
}

fun main() = DayX().solve(
    expectedAnswerForSampleInP1 = null,
    expectedAnswerForSampleInP2 = null,
)
