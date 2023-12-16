package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.AocPuzzle

fun main() = Day14().solve(
    expectedAnswerForSampleInP1 = 136,
    expectedAnswerForSampleInP2 = 64,
)

private typealias Input23d14 = MutableList<MutableList<Char>>

private class Day14: AocPuzzle<Input23d14>() {

    override fun parseInput(puzzleInput: List<String>): Input23d14 =
        puzzleInput.map { it.toMutableList() }.toMutableList()

    override fun partOne(): Int {
        tiltVertically(up = true, input)
        return input.calculateLoad()
    }

    override fun partTwo(): Int {
        val targetIterations = 1000000000L
        var iteration = 0L
        var cycleFound = false
        val memo = hashMapOf<String, Long>()

        while (iteration < targetIterations) {
            if (!cycleFound) memo[input.memoKey] = iteration

            tiltVertically(up = true, input)
            tiltHorizontally(left = true, input)
            tiltVertically(up = false, input)
            tiltHorizontally(left = false, input)

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
        return input.calculateLoad()
    }

    val Input23d14.memoKey: String
        get() = this
            .map { it.joinToString(separator = "") }
            .fold("") { acc, l -> "$acc,$l" }

    fun Input23d14.calculateLoad(): Int =
        mapIndexed { index, line ->
            line.count { it == 'O' } * (size - index)
        }.sum()

    fun tiltVertically(up: Boolean, input: Input23d14) =
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

    fun tiltHorizontally(left: Boolean, input: Input23d14) =
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
}
