package com.arblitroshani.adventofcode.year2023.day12

import com.arblitroshani.adventofcode.framework.solution
import com.arblitroshani.adventofcode.util.extension.decrementFirst
import com.arblitroshani.adventofcode.util.extension.hasSingleZeroElement
import com.arblitroshani.adventofcode.util.extension.repeat
import com.arblitroshani.adventofcode.util.extension.toIntList

private data class Record(val condition: String, val damagedSprings: String)

private typealias Input = List<Record>

fun main() = solution<Input>(2023, 12) {

    var condition = ""
    val memo = mutableMapOf<Pair<Int, List<Int>>, Long>()

    fun validCombinations(d: List<Int>, i: Int = 0, lastIsHash: Boolean = false): Long {
        if (i == condition.length) return if (d.isEmpty() || d.hasSingleZeroElement()) 1 else 0
        if (d.isEmpty()) return if (condition[i] == '#') 0 else validCombinations(d, i + 1)
        if (d[0] == 0)   return if (condition[i] == '#') 0 else validCombinations(d.drop(1), i + 1)
        if (condition[i] == '.') return if (lastIsHash)  0 else validCombinations(d, i + 1)

        val key = Pair(i, d)
        if (memo[key] == null) {
            val putHash = validCombinations(d.decrementFirst(), i + 1, true)
            val shouldPutDot = condition[i] != '#' && (!lastIsHash || d[0] == 0)
            val putDot = if (shouldPutDot) validCombinations(d, i + 1) else 0L
            memo[key] = putHash + putDot
        }
        return memo[key]!!
    }

    fun calculateValidCombinations(record: Record, repetitions: Int): Long {
        memo.clear()
        condition = record.condition.repeat(repetitions, "?")
        return validCombinations(record.damagedSprings.repeat(repetitions, ",").toIntList())
    }

    parseInput { lines ->
        lines.map { line ->
            val (cnd, dd) = line.split(' ')
            Record(cnd, dd)
        }
    }

    partOne { input ->
        input.sumOf { calculateValidCombinations(it, repetitions = 1) }
    }

    partTwo { input ->
        input.sumOf { calculateValidCombinations(it, repetitions = 5) }
    }

    val testInput = """
        ???.### 1,1,3
        .??..??...?##. 1,1,3
        ?#?#?#?#?#?#?#? 1,3,1,6
        ????.#...#... 4,1,1
        ????.######..#####. 1,6,5
        ?###???????? 3,2,1
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 21
    }

    partTwoTest {
        testInput shouldOutput 525152
    }
}
