package com.arblitroshani.adventofcode

fun main() = DayX().solve(
    expectedAnswerForSampleInP1 = null,
    expectedAnswerForSampleInP2 = null,
)

private typealias Input23dX = MutableList<MutableList<Char>>

private class DayX: AocPuzzle<Input23dX>() {

    override fun parseInput(puzzleInput: List<String>): Input23dX {
        return puzzleInput.map { it.toMutableList() }.toMutableList()
    }

    override fun partOne(): Int {
        return 0
    }

    override fun partTwo(): Int {
        return 0
    }
}
