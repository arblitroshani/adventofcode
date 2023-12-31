package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.util.InputReader
import com.arblitroshani.adventofcode.util.println

fun main() {
    val numberLines = InputReader(2023, 9).read()
        .map { it.split(" ").map(String::toInt) }

    numberLines.sumOf { resolve(it) }.println()
    numberLines.sumOf { resolve(it.reversed()) }.println()
}

fun resolve(numbers: List<Int>): Int {
    var values = numbers
    var next = 0
    while (!values.all { it == 0 }) {
        next += values.last()
        values = values.windowed(2).map { it[1] - it[0] }
    }
    return next
}
