package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.util.InputReader
import com.arblitroshani.adventofcode.util.print

fun main() =
    InputReader(2023, 9).read()
        .map { it.split(" ").map(String::toInt) }
        .map { numbers ->
            var values = numbers
            var next = 0
            val firstElements = mutableListOf<Int>()
            while (!values.all { it == 0 }) {
                next += values.last()
                firstElements.add(values[0])
                values = values.windowed(2).map { it[1] - it[0] }
            }
            next to firstElements.reversed()
        }
        .fold(0 to 0) { (sum1, sum2), (next, elements) ->
            val prev = elements.zipWithNext().fold(elements[0]) { acc, (_, b) -> b - acc }
            sum1 + next to sum2 + prev
        }
        .print()
