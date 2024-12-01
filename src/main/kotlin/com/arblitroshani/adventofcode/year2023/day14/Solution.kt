package com.arblitroshani.adventofcode.year2023.day14

import com.arblitroshani.adventofcode.framework.solution

private typealias Input = MutableList<MutableList<Char>>

private val Input.memoKey: String
    get() = this
        .map { it.joinToString(separator = "") }
        .fold("") { acc, l -> "$acc,$l" }

fun main() = solution<Input>(2023, 14) {

    fun Input.calculateLoad(): Int =
        mapIndexed { index, line ->
            line.count { it == 'O' } * (size - index)
        }.sum()

    fun tiltVertically(input: Input, up: Boolean) =
        (0 until input[0].size).forEach { c ->
            var lastFreeIndex = if (up) 0 else input.size - 1
            var rowIndexes: IntProgression = input.indices
            if (!up) rowIndexes = rowIndexes.reversed()
            val delta = if (up) 1 else -1

            rowIndexes.forEach { r ->
                if (input[r][c] == '#') {
                    lastFreeIndex = r + delta
                } else if (input[r][c] == 'O') {
                    input[lastFreeIndex][c] = 'O'
                    if (lastFreeIndex != r) input[r][c] = '.'
                    lastFreeIndex += delta
                }
            }
        }

    fun tiltHorizontally(input: Input, left: Boolean) =
        (0 until input.size).forEach { r ->
            var lastFreeIndex = if (left) 0 else input[0].size - 1
            var colIndexes: IntProgression = (0 until input[0].size)
            if (!left) colIndexes = colIndexes.reversed()
            val delta = if (left) 1 else -1

            colIndexes.forEach { c ->
                if (input[r][c] == '#') {
                    lastFreeIndex = c + delta
                } else if (input[r][c] == 'O') {
                    input[r][lastFreeIndex] = 'O'
                    if (lastFreeIndex != c) input[r][c] = '.'
                    lastFreeIndex += delta
                }
            }
        }

    parseInput { lines ->
        lines.map(String::toMutableList).toMutableList()
    }

    partOne { input ->
        tiltVertically(input, up = true)
        input.calculateLoad()
    }

    partTwo { input ->
        val targetIterations = 1000000000L
        var iteration = 0L
        var cycleFound = false
        val memo = hashMapOf<String, Long>()

        while (iteration < targetIterations) {
            if (!cycleFound) memo[input.memoKey] = iteration

            tiltVertically(input, up = true)
            tiltHorizontally(input, left = true)
            tiltVertically(input, up = false)
            tiltHorizontally(input, left = false)

            if (!cycleFound) {
                val cachedResult = memo[input.memoKey]
                if (cachedResult != null) {
                    val cycleLength = iteration - cachedResult + 1
                    while (iteration < targetIterations) iteration += cycleLength
                    iteration -= cycleLength
                    cycleFound = true
                }
            }
            iteration++
        }
        input.calculateLoad()
    }

    val testInput = """
        O....#....
        O.OO#....#
        .....##...
        OO.#O....O
        .O.....O#.
        O.#..O.#.#
        ..O..#O..O
        .......O..
        #....###..
        #OO..#....
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 136
    }

    partTwoTest {
        testInput shouldOutput 64
    }
}
