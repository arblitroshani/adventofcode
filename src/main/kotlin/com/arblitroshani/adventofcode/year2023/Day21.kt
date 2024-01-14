package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.AocPuzzle
import com.arblitroshani.adventofcode.util.common.CellIndex
import com.arblitroshani.adventofcode.util.common.get

typealias Input23d21 = List<List<Char>>

class Day21: AocPuzzle<Input23d21>() {

    private val memo = mutableMapOf<Pair<CellIndex, Int>, Set<CellIndex>>()

    override fun parseInput(lines: List<String>): Input23d21 =
        lines.map { it.toList() }

    override fun partOne(): Int {
        val startIndex = CellIndex(input.size / 2, input[0].size / 2)
        return step(startIndex, 64).count()
    }

    private fun step(index: CellIndex, remaining: Int): Set<CellIndex> {
        if (remaining < 0 || input[index] == '#') return emptySet()
        if (remaining == 0) return setOf(index)

        val memoKey = Pair(index, remaining)
        if (memo[memoKey] == null)
            memo[memoKey] = index.neighbors.fold(emptySet()) { acc, n ->
                acc + step(n, remaining - 1)
            }
        return memo[memoKey]!!
    }

    override fun partTwo(): Long = 0 // TODO
}

fun main() = Day21().solve()
