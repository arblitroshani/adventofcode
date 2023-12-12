package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.AocPuzzle
import com.arblitroshani.adventofcode.util.extension.decrementFirst
import com.arblitroshani.adventofcode.util.extension.hasSingleZeroElement
import com.arblitroshani.adventofcode.util.extension.memoKey
import com.arblitroshani.adventofcode.util.extension.repeat
import com.arblitroshani.adventofcode.util.extension.toIntList

fun main() = Day12().solve(
    expectedAnswerForSampleInP1 = 21L,
    expectedAnswerForSampleInP2 = 525152L,
)

private data class Record(val condition: String, val damagedSprings: String)

private typealias Input23d12 = List<Record>

private class Day12: AocPuzzle<Input23d12>() {

    var condition: String = ""
    val memo = mutableMapOf<String, Long>()

    override fun parseInput(input: List<String>): Input23d12 =
        input.map { line ->
            val (condition, dd) = line.split(' ')
            Record(condition, dd)
        }

    override fun partOne(input: Input23d12): Long =
        input.sumOf { calculateValidCombinations(it, repetitions = 1) }

    override fun partTwo(input: Input23d12): Long =
        input.sumOf { calculateValidCombinations(it, repetitions = 5) }

    fun calculateValidCombinations(record: Record, repetitions: Int): Long {
        memo.clear()
        condition = record.condition.repeat(repetitions, "?")
        return validCombinations(record.damagedSprings.repeat(repetitions, ",").toIntList())
    }

    private fun validCombinations(d: List<Int>, i: Int = 0, lastIsHash: Boolean = false): Long {
        if (i == condition.length) return if (d.isEmpty() || d.hasSingleZeroElement()) 1 else 0
        if (d.isEmpty()) return if (condition[i] == '#') 0 else validCombinations(d, i + 1)
        if (d[0] == 0)   return if (condition[i] == '#') 0 else validCombinations(d.drop(1), i + 1)
        if (condition[i] == '.') return if (lastIsHash)  0 else validCombinations(d, i + 1)

        val key = "$i-${d.memoKey}"
        if (memo[key] == null) {
            val putHash = validCombinations(d.decrementFirst(), i + 1, true)
            val shouldPutDot = condition[i] != '#' && (!lastIsHash || d[0] == 0)
            val putDot = if (shouldPutDot) validCombinations(d, i + 1) else 0L
            memo[key] = putHash + putDot
        }
        return memo[key]!!
    }
}
