package com.arblitroshani.adventofcode.year2023.day01

import com.arblitroshani.adventofcode.framework.solution

private typealias Input = List<String>

fun main() = solution<Input>(2023, 1) {

    fun findFirstDigit(line: String, spelledNumbers: List<String>): String {
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

    parseInput { lines ->
        lines
    }

    partOne { input ->
        input.sumOf { line ->
            var digits = line.filter(Char::isDigit)
            if (digits.count() == 1) {
                digits = "$digits$digits"
            } else if (digits.count() > 2) {
                digits = "${digits.first()}${digits.last()}"
            }
            digits.toInt()
        }
    }

    partTwo { input ->
        val spelledNumbers = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        input.sumOf { line ->
            val d1 = findFirstDigit(line, spelledNumbers)
            val d2 = findFirstDigit(line.reversed(), spelledNumbers.map(String::reversed))
            "$d1$d2".toInt()
        }
    }

    partOneTest {
        """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
        """.trimIndent() shouldOutput 142
    }

    partTwoTest {
        """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen
        """.trimIndent() shouldOutput 281
    }
}
