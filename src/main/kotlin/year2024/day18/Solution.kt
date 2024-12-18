package year2024.day18

import framework.solution
import util.common.CellIndex
import util.common.algorithms.dijkstra

fun main() = solution<List<CellIndex>>(2024, 18) {

    fun solve(size: Int, corruptedCells: Set<CellIndex>): Int {
        val target = CellIndex(size - 1, size - 1)
        val path = dijkstra(
            start = CellIndex(0, 0),
            adjacency = { node ->
                node.neighbors
                    .filter { it.isInsideBoundsOf(size) && it !in corruptedCells }
                    .map { it to 1 }
            },
            isGoal = { it == target },
        )
        return path.size - 1
    }

    fun firstByteToUnsolvable(size: Int, corruptedCells: List<CellIndex>): String {
        var startIndex = 0
        var endIndex = corruptedCells.size
        while (startIndex <= endIndex) {
            val guess = startIndex + (endIndex - startIndex) / 2
            val a = solve(size, corruptedCells.take(guess - 1).toSet())
            val b = solve(size, corruptedCells.take(guess).toSet())
            if (a > 0 && b == -1) return corruptedCells[guess - 1].let { "${it.y},${it.x}" }
            if (a > 0 && b > 0) startIndex = guess + 1 // picked too low
            else if (a == -1 && b == -1) endIndex = guess - 1 // picked too high
        }
        return "No Answer"
    }

    parseInput { lines ->
        lines.map { line ->
            line.split(',')
                .map(String::toInt)
                .let { CellIndex(it[1], it[0]) }
        }
    }

    partOne { corruptedCells ->
        solve(71, corruptedCells.take(1024).toSet())
    }

    partTwo { corruptedCells ->
        firstByteToUnsolvable(71, corruptedCells)
    }

    val testInput = """
        5,4
        4,2
        4,5
        3,0
        2,1
        6,3
        2,4
        1,5
        0,6
        3,3
        2,6
        5,1
        1,2
        5,5
        2,5
        6,5
        1,4
        0,4
        6,4
        1,1
        6,1
        1,0
        0,5
        1,6
        2,0
    """.trimIndent()

    partOneTest {
        testInput with {
            solve(7, it.take(12).toSet())
        } shouldOutput 22
    }

    partTwoTest {
        testInput with {
            firstByteToUnsolvable(7, it)
        } shouldOutput "6,1"
    }
}
