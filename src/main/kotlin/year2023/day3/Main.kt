package year2023.day3

import java.io.File

data class PartNumber(
    val row: Int,
    val startX: Int,
    val endX: Int,
    val value: Int,
) {
    fun overlapsRange(targetRow: Int, targetStart: Int, targetEnd: Int): Boolean {
        return row == targetRow && (startX in targetStart..targetEnd || endX in targetStart..targetEnd)
    }
}

val partNumbers = mutableListOf<PartNumber>()

fun main() {
    val input = File("./src/main/kotlin/year2023/day3/input.txt").readLines()

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

    println("Part1: ${partNumbers.sumOf { it.value }}")

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
                    totalGearRatios += (gearPartNumbers.first().value * gearPartNumbers.last().value)
            }
        }
    }
    println("Part2: $totalGearRatios")
}

fun addNumberToPartNumbersIfSatisfiesConditions(currentNumber: String, index: Int, currentPosition: Int, input: List<String>) {
    if (currentNumber.isEmpty()) return
    val startX = currentPosition - currentNumber.length
    val endX = currentPosition - 1
    if (!isPartNumber(index, startX, endX, input)) return
    partNumbers.add(PartNumber(index, startX, endX, currentNumber.toInt()))
}

fun isPartNumber(row: Int, startX: Int, endX: Int, input: List<String>): Boolean {
    if (row > 0) { // top
        val topRow = input[row - 1].substring(
            startIndex = Math.max(0, startX - 1),
            endIndex = Math.min(input[row - 1].length - 1, endX + 2)
        )
        if (containsSymbol(topRow)) return true
    }
    if (row < input.count() - 1) { // bottom
        val bottomRow = input[row + 1].substring(
            startIndex = Math.max(0, startX - 1),
            endIndex = Math.min(input[row + 1].length - 1, endX + 2)
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

fun containsSymbol(row: String): Boolean {
    for (character in row)
        if (!character.isDigit() && character != '.') return true
    return false
}
