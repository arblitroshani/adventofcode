package year2025.day07

import framework.solution
import util.common.CellIndex
import util.common.get

private data class Input(
    val grid: List<List<Char>>,
    val startIndex: CellIndex,
)

fun main() = solution<Input>(2025, 7) {

    parseInput { lines ->
        Input(
            grid = lines.map(String::toList),
            startIndex = CellIndex(0, lines.first().length / 2)
        )
    }

    partOne { (grid, startIndex) ->
        val visited = mutableSetOf<CellIndex>()
        fun numberOfSplits(index: CellIndex): Int = when {
            index.isOutsideBoundsOf(grid) || index in visited -> 0
            grid[index] != '^' -> numberOfSplits(index.bottom)
            else -> {
                visited.add(index)
                1 + index.bottom.sideNeighbors.sumOf(::numberOfSplits)
            }
        }
        numberOfSplits(startIndex)
    }

    partTwo { (grid, startIndex) ->
        val memo = mutableMapOf<CellIndex, Long>()
        fun numberOfTimelines(index: CellIndex): Long = when {
            index.isOutsideBoundsOf(grid) -> 1L
            grid[index] != '^' -> numberOfTimelines(index.bottom)
            else -> memo.getOrPut(index) {
                index.bottom.sideNeighbors.sumOf(::numberOfTimelines)
            }
        }
        numberOfTimelines(startIndex)
    }

    val testInput = """
        .......S.......
        ...............
        .......^.......
        ...............
        ......^.^......
        ...............
        .....^.^.^.....
        ...............
        ....^.^...^....
        ...............
        ...^.^...^.^...
        ...............
        ..^...^.....^..
        ...............
        .^.^.^.^.^...^.
        ...............
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 21
    }

    partTwoTest {
        testInput shouldOutput 40
    }
}
