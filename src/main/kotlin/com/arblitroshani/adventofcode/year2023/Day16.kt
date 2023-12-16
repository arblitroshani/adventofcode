package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.AocPuzzle
import com.arblitroshani.adventofcode.util.common.CellIndex
import com.arblitroshani.adventofcode.util.common.Dir
import com.arblitroshani.adventofcode.util.common.get
import com.arblitroshani.adventofcode.util.common.set

fun main() = Day16().solve(
    expectedAnswerForSampleInP1 = 46,
    expectedAnswerForSampleInP2 = 51,
)

typealias Input23d16 = List<List<Char>>

class Day16: AocPuzzle<Input23d16>() {

    private lateinit var visited: List<MutableList<Boolean>>
    private val memo = hashMapOf<String, Boolean>()

    override fun parseInput(puzzleInput: List<String>): Input23d16 =
        puzzleInput.map(String::toList)

    override fun partOne(): Int =
        countEnergizedTiles(0, -1, Dir.R)

    override fun partTwo(): Int {
        val rowIndices = input.first().indices
        val columnIndices = input.indices
        return maxOf(
            rowIndices.maxOf { countEnergizedTiles(-1, it, Dir.D) },
            rowIndices.maxOf { countEnergizedTiles(input.size, it, Dir.U) },
            columnIndices.maxOf { countEnergizedTiles(it, -1, Dir.R) },
            columnIndices.maxOf { countEnergizedTiles(it, input[0].size, Dir.L) },
        )
    }

    private fun countEnergizedTiles(x: Int, y: Int, d: Dir): Int {
        visited = List(input.size) { MutableList(input[0].size) { false } }
        memo.clear()
        visitTile(CellIndex(x, y), d)
        return visited.flatten().count { it }
    }

    private fun visitTile(prevIndex: CellIndex, d: Dir) {
        val i = prevIndex.next(d)
        val memoKey = "${i.memoKey}.$d"
        if (i.isOutsideBoundsOf(input) || memo[memoKey] == true) return

        memo[memoKey] = true
        visited[i] = true

        when (input[i]) {
            if (d.isVertical) '/'  else '\\' -> visitTile(i, d.nextCw)
            if (d.isVertical) '\\' else '/'  -> visitTile(i, d.nextCcw)
            if (d.isVertical) '-'  else '|'  -> { visitTile(i, d.nextCw); visitTile(i, d.nextCcw) }
            else -> visitTile(i, d)
        }
    }
}
