package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.util.InputReader
import com.arblitroshani.adventofcode.util.println

fun main() {
    val input = InputReader(2023, 4).read()
    val copies = Array(input.size) { 1 }
    val sumPt1 = input.mapIndexed { index, line ->
        line.split(":").last().trim()
            .split("|").map { it.split(" ").filterNot(String::isBlank).toSet() }
            .reduce(Set<String>::intersect).size
            .let { ticketsWon ->
                for (i in 1 .. ticketsWon) copies[index + i] += copies[index]
                1 shl ticketsWon - 1
            }
    }.sum()

    sumPt1.println()
    copies.sum().println()
}
