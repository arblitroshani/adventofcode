package year2024.day18

import framework.solution
import util.common.CellIndex
import util.common.algorithms.dijkstra

fun main() = solution<List<CellIndex>>(2024, 18) {

    fun solve(size: Int, corrupted: Set<CellIndex>): Int {
        val target = CellIndex(size - 1, size - 1)
        val path = dijkstra(
            start = CellIndex(0, 0),
            adjacency = { node ->
                node.neighbors
                    .filter { it.isInsideBoundsOf(size) && it !in corrupted }
                    .map { it to 1 }
            },
            isGoal = { it == target },
        )
        return path.size - 1
    }

    fun firstByteToUnsolvable(size: Int, corrupted: List<CellIndex>): String {
        var startIndex = 0
        var endIndex = corrupted.size
        while (startIndex <= endIndex) {
            val guess = startIndex + (endIndex - startIndex) / 2
            val a = solve(size, corrupted.take(guess).toSet())
            val b = solve(size, corrupted.take(guess + 1).toSet())
            if (a > 0 && b == -1) return corrupted[guess].let { "${it.c},${it.r}" }
            if (a > 0) startIndex = guess + 1 // too low
            else endIndex = guess - 1 // too high
        }
        error("No Answer")
    }

    parseInput { lines ->
        lines.map { line ->
            line.split(',')
                .map(String::toInt)
                .let { (c, r) -> CellIndex(r, c) }
        }
    }

    partOne { solve(71, it.take(1024).toSet()) }
    partTwo { firstByteToUnsolvable(71, it) }

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
