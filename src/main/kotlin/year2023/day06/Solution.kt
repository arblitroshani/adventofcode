package year2023.day06

import framework.solution

data class Input(val durations: List<Long>, val recordDistances: List<Long>)

fun main() = solution<Input>(2023, 6) {

    data class Race(val duration: Long, val recordDistance: Long) {
        val numberOfWaysToWin =
            (1 .. duration)
                .map { (duration - it) * it }
                .count { it > recordDistance }
    }

    fun extractNumbersFromLine(line: String): List<Long> =
        line
            .substringAfter(':')
            .trim()
            .split(' ')
            .filterNot(String::isEmpty)
            .map(String::trim)
            .map(String::toLong)

    parseInput { lines ->
        Input(
            durations = extractNumbersFromLine(line = lines[0]),
            recordDistances = extractNumbersFromLine(line = lines[1]),
        )
    }

    partOne { input ->
        input.durations
            .mapIndexed { i, duration -> Race(duration, input.recordDistances[i]) }
            .map(Race::numberOfWaysToWin)
            .reduce(Int::times)
    }

    partTwo { input ->
        Race(
            duration = input.durations.joinToString(separator = "").toLong(),
            recordDistance = input.recordDistances.joinToString(separator = "").toLong(),
        ).numberOfWaysToWin
    }

    val testInput = """
        Time:      7  15   30
        Distance:  9  40  200
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 288
    }

    partTwoTest {
        testInput shouldOutput 71503
    }
}
