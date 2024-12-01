package com.arblitroshani.adventofcode.year2023.day04

import com.arblitroshani.adventofcode.framework.solution

private typealias Input = List<String>

fun main() = solution<Input>(2023, 4) {

    var copies: Array<Int> = emptyArray()

    fun commonPartOne(input: Input): List<Int> {
        copies = Array(input.size) { 1 }
        return input.mapIndexed { index, line ->
            line.split(":").last().trim()
                .split("|")
                .map { it.split(" ").filterNot(String::isBlank).toSet() }
                .reduce(Set<String>::intersect).size
                .let { ticketsWon ->
                    for (i in 1 .. ticketsWon) copies[index + i] += copies[index]
                    1 shl ticketsWon - 1
                }
        }
    }

    parseInput { lines ->
        lines
    }

    partOne { input ->
        commonPartOne(input).sum()
    }

    partTwo { input ->
        commonPartOne(input)
        copies.sum()
    }

    val testInput = """
        Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
        Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
        Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
        Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
        Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 13
    }

    partTwoTest {
        testInput shouldOutput 30
    }
}
