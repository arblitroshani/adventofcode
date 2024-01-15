package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.AocPuzzle

typealias Input23d03 = List<String>

class Day03: AocPuzzle<Input23d03>() {

    data class PartNumber(val row: Int, val startX: Int, val endX: Int, val value: Int) {
        fun overlapsRange(targetRow: Int, targetStart: Int, targetEnd: Int): Boolean =
            row == targetRow && (startX in targetStart..targetEnd || endX in targetStart..targetEnd)
    }

    private val partNumbers = mutableListOf<PartNumber>()

    override fun parseInput(lines: List<String>): Input23d03 {
        partNumbers.clear()
        return lines
    }

    override fun partOne(): Int {
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
        return partNumbers.sumOf(PartNumber::value)
    }

    override fun partTwo(): Int {
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
        return totalGearRatios
    }

    private fun addNumberToPartNumbersIfSatisfiesConditions(
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

    private fun isPartNumber(row: Int, startX: Int, endX: Int, input: List<String>): Boolean {
        if (row > 0) { // top
            val topRow = input[row - 1].substring(
                startIndex = maxOf(0, startX - 1),
                endIndex = minOf(input[row - 1].length - 1, endX + 2)
            )
            if (containsSymbol(topRow)) return true
        }
        if (row < input.count() - 1) { // bottom
            val bottomRow = input[row + 1].substring(
                startIndex = maxOf(0, startX - 1),
                endIndex = minOf(input[row + 1].length - 1, endX + 2)
            )
            if (containsSymbol(bottomRow)) return true
        }
        if (startX > 0) { // left
            val left = input[row].substring(startX - 1, startX)
            if (containsSymbol(left)) return true
        }
        if (endX < input[row].length - 1) { // right
            val right = input[row].substring(endX + 1, endX + 2)
            if (containsSymbol(right)) return true
        }
        return false
    }

    private fun containsSymbol(row: String) = row.any { !it.isDigit() && it != '.' }
}

fun main() = Day03().solve(
    expectedAnswerForSampleInP1 = 4361,
    expectedAnswerForSampleInP2 = 467835,
)
