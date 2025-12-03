package year2025.day03

import framework.solution
import util.pow

fun main() = solution<List<List<Int>>>(2025, 3) {

    parseInput { lines ->
        lines.map { it.map(Char::digitToInt) }
    }

    fun outputJoltage(list: List<Int>, remaining: Int): Long {
        if (remaining == 0) { return 0L }
        val section = list.dropLast(remaining - 1)
        val biggest = section.max()
        val position = section.indexOfFirst { it == biggest }
        return biggest * (10L.pow(remaining - 1)) + outputJoltage(list.drop(position + 1), remaining - 1)
    }

    partOne { input ->
        input.sumOf { outputJoltage(it, 2) }
    }

    partTwo { input ->
        input.sumOf { outputJoltage(it, 12) }
    }

    val testInput = """
        987654321111111
        811111111111119
        234234234234278
        818181911112111
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 357L
    }

    partTwoTest {
        testInput shouldOutput 3121910778619L
    }
}
