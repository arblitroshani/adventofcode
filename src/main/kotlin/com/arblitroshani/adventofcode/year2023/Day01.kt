package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.AocPuzzle

typealias Input23d01 = List<String>

class Day01: AocPuzzle<Input23d01>() {

    override fun parseInput(lines: List<String>) = lines

    override fun partOne(): Int = input.sumOf { line ->
        var digits = line.filter(Char::isDigit)
        if (digits.count() == 1) {
            digits = "$digits$digits"
        } else if (digits.count() > 2) {
            digits = "${digits.first()}${digits.last()}"
        }
        digits.toInt()
    }

    override fun partTwo(): Int {
        val spelledNumbers = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        return input.sumOf { line ->
            val d1 = findFirstDigit(line, spelledNumbers)
            val d2 = findFirstDigit(line.reversed(), spelledNumbers.map(String::reversed))
            "$d1$d2".toInt()
        }
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

fun main() = Day01().solve(
    expectedAnswerForSampleInP1 = 142,
)
