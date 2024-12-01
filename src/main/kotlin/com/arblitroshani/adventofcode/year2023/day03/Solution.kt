package com.arblitroshani.adventofcode.year2023.day03

import com.arblitroshani.adventofcode.framework.solution

private typealias Input = List<String>

data class PartNumber(val row: Int, val startX: Int, val endX: Int, val value: Int) {
    fun overlapsRange(targetRow: Int, targetStart: Int, targetEnd: Int): Boolean =
        row == targetRow && (startX in targetStart..targetEnd || endX in targetStart..targetEnd)
}

private fun String.containsSymbol() = any { !it.isDigit() && it != '.' }

fun main() = solution<Input>(2023, 3) {

    val partNumbers = mutableListOf<PartNumber>()

    fun isPartNumber(row: Int, startX: Int, endX: Int, input: List<String>): Boolean {
        if (row > 0) { // top
            val topRow = input[row - 1].substring(
                startIndex = maxOf(0, startX - 1),
                endIndex = minOf(input[row - 1].length - 1, endX + 2)
            )
            if (topRow.containsSymbol()) return true
        }
        if (row < input.count() - 1) { // bottom
            val bottomRow = input[row + 1].substring(
                startIndex = maxOf(0, startX - 1),
                endIndex = minOf(input[row + 1].length - 1, endX + 2)
            )
            if (bottomRow.containsSymbol()) return true
        }
        if (startX > 0) { // left
            val left = input[row].substring(startX - 1, startX)
            if (left.containsSymbol()) return true
        }
        if (endX < input[row].length - 1) { // right
            val right = input[row].substring(endX + 1, endX + 2)
            if (right.containsSymbol()) return true
        }
        return false
    }

    fun addNumberToPartNumbersIfSatisfiesConditions(
        currentNumber: String,
        index: Int,
        currentPosition: Int,
        input: List<String>,
    ) {
        if (currentNumber.isEmpty()) return
        val startX = currentPosition - currentNumber.length
        val endX = currentPosition - 1
        if (!isPartNumber(index, startX, endX, input)) return
        partNumbers.add(PartNumber(index, startX, endX, currentNumber.toInt()))
    }

    parseInput { lines ->
        lines
    }

    partOne { input ->
        partNumbers.clear()
        input.forEachIndexed { index, line ->
            var currentNumber = ""
            var currentPosition = 0
            while (currentPosition < line.length) {
                if (line[currentPosition].isDigit()) {
                    currentNumber += line[currentPosition]
                } else {
                    addNumberToPartNumbersIfSatisfiesConditions(currentNumber, index, currentPosition, input)
                    currentNumber = ""
                }
                currentPosition += 1
            }
            addNumberToPartNumbersIfSatisfiesConditions(currentNumber, index, currentPosition, input)
        }
        partNumbers.sumOf(PartNumber::value)
    }

    partTwo { input ->
        var totalGearRatios = 0
        input.forEachIndexed { index, line ->
            line.forEachIndexed { pos, character ->
                if (character == '*') {
                    val gearPartNumbers = partNumbers.filter {
                        it.overlapsRange(index - 1, pos - 1, pos + 1) ||
                                it.overlapsRange(index + 1, pos - 1, pos + 1) ||
                                it.overlapsRange(index + 0, pos - 1, pos - 1) ||
                                it.overlapsRange(index + 0, pos + 1, pos + 1)
                    }
                    if (gearPartNumbers.count() == 2)
                        totalGearRatios += (gearPartNumbers[0].value * gearPartNumbers[1].value)
                }
            }
        }
        totalGearRatios
    }

    val testInput = """
        467..114..
        ...*......
        ..35..633.
        ......#...
        617*......
        .....+.58.
        ..592.....
        ......755.
        ...$.*....
        .664.598..
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 4361
    }
}
