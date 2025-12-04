package year2025.day04

import framework.solution
import util.common.CellIndex
import util.common.get
import util.common.set

private typealias Input = List<MutableList<Char>>

fun main() = solution(2025, 4) {

    parseInput { lines ->
        lines.map(String::toMutableList)
    }

    fun cellsToRemove(input: Input, remove: Boolean): Int =
        input.indices.sumOf { r ->
            input[r].indices
                .map { c -> CellIndex(r, c) }
                .filter { input[it] != '.' }
                .map { cellIndex ->
                    cellIndex to cellIndex.allNeighbors.filter { it.isInsideBoundsOf(input) }
                }
                .count { (cellIndex, neighborIndexes) ->
                    val rollsOfPaper = neighborIndexes.count { input[it] == '@' }
                    val isValid = rollsOfPaper < 4
                    if (isValid && remove) input[cellIndex] = '.'
                    isValid
                }
        }

    partOne { input ->
        cellsToRemove(input, remove = false)
    }

    partTwo { input ->
        var cnt = 0
        do {
            val toRemove = cellsToRemove(input, remove = true)
            cnt += toRemove
        } while (toRemove > 0)
        cnt
    }

    val testInput = """
        ..@@.@@@@.
        @@@.@.@.@@
        @@@@@.@.@@
        @.@@@@..@.
        @@.@@@@.@@
        .@@@@@@@.@
        .@.@.@.@@@
        @.@@@.@@@@
        .@@@@@@@@.
        @.@.@@@.@.
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 13
    }

    partTwoTest {
        testInput shouldOutput 43
    }
}
