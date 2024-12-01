package year2024.day01

import framework.solution
import kotlin.math.abs

private data class Input(
    val firstLocations: List<Int>,
    val secondLocations: List<Int>,
)

fun main() = solution<Input>(2024, 1) {

    parseInput { lines ->
        val locationIdLines = lines.map {
            it
                .split(' ')
                .filterNot(String::isBlank)
                .map(String::toInt)
        }
        Input(
            locationIdLines.map { it[0] }.sorted(),
            locationIdLines.map { it[1] }.sorted(),
        )
    }

    partOne { (firstLocations, secondLocations) ->
        firstLocations
            .zip(secondLocations)
            .sumOf { (loc1, loc2) -> abs(loc1 - loc2) }
    }

    partTwo { (firstLocations, secondLocations) ->
        firstLocations.sumOf { n ->
            n * secondLocations.count { it == n }
        }
    }

    val testInput = """
        3   4
        4   3
        2   5
        1   3
        3   9
        3   3
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 11
    }

    partTwoTest {
        testInput shouldOutput 31
    }
}
