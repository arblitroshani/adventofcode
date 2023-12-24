package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.util.InputReader
import com.arblitroshani.adventofcode.util.println

fun main() {
    val input = InputReader(2023, 1).read()
    firstPart(input).println()
    secondPart(input).println()
}

fun firstPart(input: List<String>): Int =
    input.sumOf { line ->
        var digits = line.filter(Char::isDigit)
        if (digits.count() == 1) {
            digits = "$digits$digits"
        } else if (digits.count() > 2) {
            digits = "${digits.first()}${digits.last()}"
        }
        digits.toInt()
    }

fun secondPart(input: List<String>): Int {
    val spelledNumbers = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    return input.sumOf { line ->
        val d1 = findFirstDigit(line, spelledNumbers)
        val d2 = findFirstDigit(line.reversed(), spelledNumbers.map(String::reversed))
        "$d1$d2".toInt()
    }
}

private fun findFirstDigit(line: String, spelledNumbers: List<String>): String {
    var digit = ""
    var index = 0

    while (digit.isEmpty()) {
        if (line[index].isDigit()) {
            digit = line[index].toString()
            break
        }
        val substringToCheck = line.substring(index, minOf(index + 5, line.length - 1))
        for (spelledNumber in spelledNumbers)
            if (substringToCheck.startsWith(spelledNumber)) {
                digit = "${spelledNumbers.indexOf(spelledNumber) + 1}"
                break
            }
        index += 1
    }
    return digit
}
