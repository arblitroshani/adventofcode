package year2025.day05

import framework.solution
import util.InputParsing

private data class Input(
    val ranges: List<LongRange>,
    val ingredients: List<Long>,
)

fun main() = solution<Input>(2025, 5) {

    parseInput { lines ->
        val (rangeLines, ingredientsLines) = InputParsing.splitListByEmptyLines(lines)

        Input(
            ranges = rangeLines
                .map {
                    val longs = it.split('-').map(String::toLong)
                    longs[0] .. longs[1]
                }
                .sortedBy { it.first },
            ingredients = ingredientsLines.map(String::toLong),
        )
    }

    partOne { input ->
        input.ingredients.count { ingredient ->
            input.ranges.any { it.contains(ingredient) }
        }
    }

    partTwo { input ->
        fun overlap(a: LongRange, b: LongRange): Boolean {
            val max = maxOf(a.first, a.last)
            val min = minOf(a.first, a.last)
            return (b.first in min..max) || (b.last in min..max)
        }

        val mergedRanges = mutableSetOf<LongRange>()

        input.ranges.forEach { range ->
            val overlapping = mergedRanges.filter { overlap(it, range) }.toSet() + setOf(range)
            val merged = overlapping.minOf { it.first } .. overlapping.maxOf { it.last }
            mergedRanges.removeAll(overlapping)
            mergedRanges.add(merged)
        }

        mergedRanges.sumOf { it.last - it.first + 1L }
    }

    val testInput = """
        3-5
        10-14
        16-20
        12-18

        1
        5
        8
        11
        17
        32
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 3
    }

    partTwoTest {
        testInput shouldOutput 14
    }
}
