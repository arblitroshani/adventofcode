package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.AocPuzzle

typealias Input23d04 = List<String>

class Day04: AocPuzzle<Input23d04>() {

    private var copies: Array<Int> = emptyArray()

    override fun parseInput(lines: List<String>): Input23d04 {
        copies = Array(lines.size) { 1 }
        return lines
    }

    override fun partOne(): Int =
        input.mapIndexed { index, line ->
            line.split(":").last().trim()
                .split("|")
                .map { it.split(" ").filterNot(String::isBlank).toSet() }
                .reduce(Set<String>::intersect).size
                .let { ticketsWon ->
                    for (i in 1 .. ticketsWon) copies[index + i] += copies[index]
                    1 shl ticketsWon - 1
                }
        }.sum()

    override fun partTwo(): Int = copies.sum()
}

fun main() = Day04().solve(
    expectedAnswerForSampleInP1 = 13,
    expectedAnswerForSampleInP2 = 30,
)
