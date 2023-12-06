package year2023.day6

import java.io.File

data class Race(val duration: Long, val recordDistance: Long) {
    val numberOfWaysToWin
        = (1 ..< duration - 1)
            .map { (duration - it) * it }
            .count { it > recordDistance }
}

fun main() {
    val input = File("./src/main/kotlin/year2023/day6/input.txt").readLines()

    // MARK: - Part 1
    val durations = extractNumbersFromLine(line = input[0])
    val recordDistances = extractNumbersFromLine(line = input[1])
    val numberOfWaysToBeatRecord = durations
        .mapIndexed { i, duration -> Race(duration, recordDistances[i]) }
        .map(Race::numberOfWaysToWin)
        .reduce(Int::times)
    println("Solution pt1: $numberOfWaysToBeatRecord")

    // MARK: - Part 2
    val race = Race(
        duration = extractNumberFromLine(line = input[0]),
        recordDistance = extractNumberFromLine(line = input[1]),
    )
    println("Solution pt2: ${race.numberOfWaysToWin}")
}

fun extractNumbersFromLine(line: String): List<Long>
    = line
        .substringAfter(':')
        .trim()
        .split(' ')
        .filterNot(String::isEmpty)
        .map(String::trim)
        .map(String::toLong)

fun extractNumberFromLine(line: String): Long
    = line
        .substringAfter(':')
        .trim()
        .filterNot { it == ' '}
        .toLong()
