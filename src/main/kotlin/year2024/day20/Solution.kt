package year2024.day20

import framework.solution
import util.common.CellIndex
import util.common.algorithms.dijkstra
import util.common.get
import util.common.set

private data class Input(
    val grid: List<List<Int>>,
    val solutionPath: List<CellIndex>,
)

fun main() = solution<Input>(2024, 20) {

    fun numberOfShortcuts(input: Input, minDiff: Int, maxCheats: Int): Int =
        input.solutionPath.sumOf { index ->
            var matches = 0
            for (rOffset in -maxCheats .. maxCheats) {
                for (cOffset in -maxCheats .. maxCheats) {
                    val newIndex = CellIndex(index.r + rOffset, index.c + cOffset)
                    val dist = index.manhattanDistance(newIndex)
                    if (
                        newIndex.isOutsideBoundsOf(input.grid) ||
                        dist !in 1..maxCheats ||
                        input.grid[newIndex] - input.grid[index] - dist < minDiff
                    ) continue
                    matches++
                }
            }
            matches
        }

    parseInput { lines ->
        lateinit var startIndex: CellIndex
        lateinit var endIndex: CellIndex
        val grid = lines.mapIndexed { r, line ->
            line.mapIndexed line@ { c, ch ->
                if (ch == '#') return@line -1
                if (ch == 'S') startIndex = CellIndex(r, c)
                if (ch == 'E') endIndex = CellIndex(r, c)
                0
            }.toMutableList()
        }
        val solutionPath = dijkstra(
            start = startIndex,
            adjacency = { index ->
                index.neighbors
                    .filter { it.isInsideBoundsOf(grid) && grid[it] >= 0 }
                    .map { it to 1L }
            },
            isGoal = { it == endIndex }
        ).map { it.first }
        solutionPath.forEachIndexed { i, index -> grid[index] = i }
        Input(grid, solutionPath)
    }

    partOne { numberOfShortcuts(input = it, minDiff = 100, maxCheats = 2) }
    partTwo { numberOfShortcuts(input = it, minDiff = 100, maxCheats = 20) }

    val testInput = """
        ###############
        #...#...#.....#
        #.#.#.#.#.###.#
        #S#...#.#.#...#
        #######.#.#.###
        #######.#.#...#
        #######.#.###.#
        ###..E#...#...#
        ###.#######.###
        #...###...#...#
        #.#####.#.###.#
        #.#...#.#.#...#
        #.#.#.#.#.#.###
        #...#...#...###
        ###############
    """.trimIndent()

    partOneTest {
        testInput with {
            numberOfShortcuts(input = it, minDiff = 10, maxCheats = 2)
        } shouldOutput 10
    }

    partTwoTest {
        testInput with {
            numberOfShortcuts(input = it, minDiff = 50, maxCheats = 20)
        } shouldOutput 285
    }
}
