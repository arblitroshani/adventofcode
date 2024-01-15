package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.AocPuzzle

data class Race(val duration: Long, val recordDistance: Long) {
    val numberOfWaysToWin =
        (1 .. duration)
            .map { (duration - it) * it }
            .count { it > recordDistance }
}

data class Input23d06(
    val durations: List<Long>,
    val recordDistances: List<Long>,
)

class Day06: AocPuzzle<Input23d06>() {

    override fun parseInput(lines: List<String>) = Input23d06(
        durations = extractNumbersFromLine(line = lines[0]),
        recordDistances = extractNumbersFromLine(line = lines[1]),
    )

    override fun partOne(): Int =
        input.durations
            .mapIndexed { i, duration -> Race(duration, input.recordDistances[i]) }
            .map(Race::numberOfWaysToWin)
            .reduce(Int::times)

    override fun partTwo(): Int =
        Race(
            duration = input.durations.joinToString(separator = "").toLong(),
            recordDistance = input.recordDistances.joinToString(separator = "").toLong(),
        ).numberOfWaysToWin

    private fun extractNumbersFromLine(line: String): List<Long> =
        line
            .substringAfter(':')
            .trim()
            .split(' ')
            .filterNot(String::isEmpty)
            .map(String::trim)
            .map(String::toLong)
}

fun main() = Day06().solve(
    expectedAnswerForSampleInP1 = 288,
    expectedAnswerForSampleInP2 = 71503,
)
