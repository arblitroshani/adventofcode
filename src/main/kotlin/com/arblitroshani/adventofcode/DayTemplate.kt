package com.arblitroshani.adventofcode

import com.arblitroshani.adventofcode.util.print

fun main() = DayX().solve(
    expectedAnswerForSampleInP1 = null,
    expectedAnswerForSampleInP2 = null,
)

//private typealias Input23dX = List<List<Int>>

private data class Input23dX(
    val commands: String,
    val numbers: List<List<Int>>,
)

private class DayX: AocPuzzle<Input23dX>() {

    override fun parseInput(input: List<String>): Input23dX {
        val commands = input[0]
        val numbers = input.drop(2).map { it.split(" ").map(String::toInt) }
        return Input23dX(commands, numbers)
    }

    override fun partOne(input: Input23dX): Int {
        return 0
    }

    override fun partTwo(input: Input23dX): Int {
        return 0
    }
}
