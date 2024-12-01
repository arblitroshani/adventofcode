package com.arblitroshani.adventofcode.year2023.day09

import com.arblitroshani.adventofcode.framework.solution

private typealias Input = List<List<Int>>

fun main() = solution<Input>(2023, 9) {

    fun resolve(numbers: List<Int>): Int {
        var values = numbers
        var next = 0
        while (!values.all { it == 0 }) {
            next += values.last()
            values = values.windowed(2).map { it[1] - it[0] }
        }
        return next
    }

    parseInput { lines ->
        lines.map { it.split(" ").map(String::toInt) }
    }

    partOne { input ->
        input.sumOf(::resolve)
    }

    partTwo { input ->
        input.sumOf { resolve(it.reversed()) }
    }

    val testInput = """
        0 3 6 9 12 15
        1 3 6 10 15 21
        10 13 16 21 30 45
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 114
    }

    partTwoTest {
        testInput shouldOutput 2
    }
}
