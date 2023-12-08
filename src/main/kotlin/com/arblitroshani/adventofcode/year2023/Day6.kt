package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.util.InputReader
import com.arblitroshani.adventofcode.util.print

data class Race(val duration: Long, val recordDistance: Long) {
    val numberOfWaysToWin =
        (1 .. duration)
            .map { (duration - it) * it }
            .count { it > recordDistance }
}

fun main() {
    val input = InputReader(2023, 6).read()

    // MARK: - Part 1
    val durations = extractNumbersFromLine(line = input[0])
    val recordDistances = extractNumbersFromLine(line = input[1])
    durations
        .mapIndexed { i, duration -> Race(duration, recordDistances[i]) }
        .map(Race::numberOfWaysToWin)
        .reduce(Int::times)
        .print()

    // MARK: - Part 2
    Race(
        duration = extractNumberFromLine(line = input[0]),
        recordDistance = extractNumberFromLine(line = input[1]),
    ).numberOfWaysToWin.print()
}

fun extractNumbersFromLine(line: String): List<Long> =
    line
        .substringAfter(':')
        .trim()
        .split(' ')
        .filterNot(String::isEmpty)
        .map(String::trim)
        .map(String::toLong)

fun extractNumberFromLine(line: String): Long =
    line
        .substringAfter(':')
        .trim()
        .filterNot { it == ' '}
        .toLong()
