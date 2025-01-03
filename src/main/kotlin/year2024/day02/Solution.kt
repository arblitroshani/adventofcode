package year2024.day02

import framework.solution

private typealias Report = List<Int>

fun main() = solution<List<Report>>(2024, 2) {

    fun isSafe(report: Report): Boolean {
        val diffs = report
            .windowed(2)
            .map { (a, b) -> a - b }
        return diffs.all { it in 1..3 } || diffs.all { it in -3..-1 }
    }

    parseInput { lines ->
        lines.map { it.split(' ').map(String::toInt) }
    }

    partOne { input ->
        input.count(::isSafe)
    }

    partTwo { input ->
        input.count { report ->
            List(report.size) { i -> isSafe(report.toMutableList().apply { removeAt(i) }) }
                .any { it }
        }
    }

    val testInput = """
        7 6 4 2 1
        1 2 7 8 9
        9 7 6 2 1
        1 3 2 4 5
        8 6 4 4 1
        1 3 6 7 9
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 2
    }

    partTwoTest {
        testInput shouldOutput 4
    }
}
