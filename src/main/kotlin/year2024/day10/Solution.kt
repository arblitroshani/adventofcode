package year2024.day10

import framework.solution
import util.common.CellIndex
import util.common.get

fun main() = solution(2024, 10) {

    fun solve(grid: List<List<Char>>, requireUnique: Boolean): Int {
        fun paths(start: CellIndex, p: CellIndex, shouldBe: Char): List<Pair<CellIndex, CellIndex>> =
            if (p.isOutsideBoundsOf(grid) || grid[p] != shouldBe) emptyList()
            else if (grid[p] == '9') listOf(start to p)
            else p.neighbors.flatMap { paths(start, it, shouldBe + 1) }
        val trailHeads = grid.flatMapIndexed { r, line ->
            line.mapIndexedNotNull { c, ch -> if (ch == '0') CellIndex(r, c) else null }
        }
        return trailHeads
            .flatMap { paths(it, it, '0') }
            .let { if (requireUnique) it.distinct() else it }
            .count()
    }

    parseInput { it.map(String::toList) }
    partOne { solve(it, requireUnique = true) }
    partTwo { solve(it, requireUnique = false) }

    val testInput = """
        89010123
        78121874
        87430965
        96549874
        45678903
        32019012
        01329801
        10456732
    """.trimIndent()

    partOneTest { testInput shouldOutput 36 }
    partTwoTest { testInput shouldOutput 81 }
}
