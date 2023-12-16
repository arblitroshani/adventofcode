package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.AocPuzzle
import com.arblitroshani.adventofcode.util.extension.columns

fun main() = Day13().solve(
    expectedAnswerForSampleInP1 = 405,
    expectedAnswerForSampleInP2 = 400,
)

private typealias Input23d13 = List<List<String>>

private data class Mirror(val l: Int, val r: Int, val t: Int, val b: Int) {

    private fun values(width: Int, height: Int): List<Int> {
        val reflections = mutableListOf<Int>()
        if (l > 0) reflections.add(l)
        if (r > 0) reflections.add(width - r)
        if (t > 0) reflections.add(100 * t)
        if (b > 0) reflections.add(100 * (height - b))
        return reflections
    }

    companion object {
        private fun from(pattern: List<String>) = Mirror(
            l = horizontalMirror(pattern.columns()),
            r = horizontalMirror(pattern.columns().reversed()),
            t = horizontalMirror(pattern),
            b = horizontalMirror(pattern.reversed()),
        )

        fun valuesFrom(pattern: List<String>) = from(pattern).values(pattern[0].length, pattern.size)

        fun firstValueFrom(pattern: List<String>) = valuesFrom(pattern).first()

        private fun horizontalMirror(pattern: List<String>): Int {
            var mutablePattern = pattern.toMutableList()
            while (mutablePattern.isNotEmpty()) {
                if (isSymmetrical(mutablePattern)) return mutablePattern.size / 2
                mutablePattern = mutablePattern.dropLast(1).toMutableList()
            }
            return 0
        }

        private fun isSymmetrical(pattern: List<String>): Boolean {
            if (pattern.size % 2 == 1) return false
            for (i in 0 until pattern.size / 2)
                if (pattern[i] != pattern[pattern.size - 1 - i]) return false
            return true
        }
    }
}

private class Day13: AocPuzzle<Input23d13>() {

    override fun parseInput(puzzleInput: List<String>): Input23d13 =
        puzzleInput.fold(mutableListOf<MutableList<String>>()) { acc, line ->
            if (line.isBlank()) {
                acc.add(mutableListOf())
            } else {
                if (acc.isEmpty()) acc.add(mutableListOf())
                acc.last().add(line)
            }
            acc
        }

    override fun partOne(): Int = input.sumOf { Mirror.firstValueFrom(it) }

    override fun partTwo(): Int {
        var sum = 0
        loop@ for (pattern in input) {
            val mutatedPattern = pattern.toMutableList()
            for ((i, line) in pattern.withIndex())
                for ((j, char) in line.withIndex()) {
                    val newChar = if (char == '#') '.' else '#'
                    mutatedPattern[i] = pattern[i].substring(0, j) + newChar + pattern[i].substring(j + 1)
                    val mirrorValues = Mirror.valuesFrom(mutatedPattern)
                        .toMutableList()
                        .apply { remove(Mirror.firstValueFrom(pattern)) }
                    mutatedPattern[i] = pattern[i]
                    if (mirrorValues.isNotEmpty()) {
                        sum += mirrorValues[0]
                        continue@loop
                    }
                }
        }
        return sum
    }
}
