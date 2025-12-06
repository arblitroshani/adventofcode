package year2025.day06

import framework.solution

private data class Input(
    val raw: List<String>,
    val numberLines: List<List<Int>>,
    val signs: List<Char>,
)

fun main() = solution<Input>(2025, 6) {

    parseInput { lines ->
        val maxLineSize = lines.maxOf(String::length)
        Input(
            raw = lines.map { line ->
                line + " ".repeat(maxLineSize - line.length)
            },
            numberLines = lines.dropLast(1)
                .map { line ->
                    line.split(' ')
                        .filter(String::isNotBlank)
                        .map(String::toInt)
                },
            signs = lines.last()
                .split(' ')
                .filter(String::isNotBlank)
                .map(String::first),
        )
    }

    partOne { input ->
        var sum = 0L
        input.signs.forEachIndexed { index, sign ->
            val initialValue = if (sign == '+') 0L else 1L
            sum += input.numberLines.fold(initialValue) { acc, line ->
                if (sign == '+') acc + line[index] else acc * line[index]
            }
        }
        sum
    }

    partTwo { input ->
        var sum = 0L

        val numberRows = input.raw.dropLast(1)
        val rowLength = numberRows[0].length

        var index = 0
        while (index < rowLength) {
            val sign = input.raw.last()[index]
            var localResult = if (sign == '+') 0L else 1L
            while (index < rowLength && numberRows.map { it[index] }.any { it != ' ' }) {
                val number = numberRows
                    .map { it[index] }
                    .filter { it != ' ' }
                    .joinToString("")
                    .toLong()
                if (sign == '+') localResult += number else localResult *= number
                index++
            }
            index++
            sum += localResult
        }

        sum
    }

    val testInput = """
        123 328  51 64 
         45 64  387 23 
          6 98  215 314
        *   +   *   +  
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 4277556
    }

    partTwoTest {
        testInput shouldOutput 3263827
    }
}
