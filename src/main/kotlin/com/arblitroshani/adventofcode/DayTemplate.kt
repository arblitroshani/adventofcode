package com.arblitroshani.adventofcode

fun main() = DayX().solve(
    expectedAnswerForSampleInP1 = null,
    expectedAnswerForSampleInP2 = null,
)

private typealias Input23dX = MutableList<MutableList<Char>>

private class DayX: AocPuzzle<Input23dX>() {

    override fun parseInput(input: List<String>): Input23dX {
        return input.map { it.toMutableList() }.toMutableList()
    }

    override fun partOne(input: Input23dX): Int {
        return 0
    }

    override fun partTwo(input: Input23dX): Int {
        return 0
    }
}
