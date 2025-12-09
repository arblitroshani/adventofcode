package year2025.day09

import framework.solution
import util.common.CellIndex
import util.extension.getAllPairs
import util.extension.toIntList
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() = solution<List<CellIndex>>(2025, 9) {

    parseInput { lines ->
        lines.map {
            val (c, r) = it.toIntList()
            CellIndex(r, c)
        }
    }

    fun surfaceOfRectangle(i1: CellIndex, i2: CellIndex): Long =
        (abs(i1.r - i2.r) + 1L) * (abs(i1.c - i2.c) + 1L)

    partOne { input ->
        input.getAllPairs()
            .maxOf { (i1, i2) -> surfaceOfRectangle(i1, i2) }
    }

    partTwo { input ->
        val verticalEdges = mutableSetOf<Pair<CellIndex, CellIndex>>()
        val horizontalEdges = mutableSetOf<Pair<CellIndex, CellIndex>>()

        fun addEdge(i1: CellIndex, i2: CellIndex) {
            if (i1.r == i2.r) horizontalEdges.add(Pair(CellIndex(i1.r, min(i1.c, i2.c)), CellIndex(i1.r, max(i1.c, i2.c))))
            if (i1.c == i2.c) verticalEdges.add(Pair(CellIndex(min(i1.r, i2.r), i1.c), CellIndex(max(i1.r, i2.r), i1.c)))
        }

        fun isValidRectangle(i1: CellIndex, i2: CellIndex): Boolean {
            val left = min(i1.c, i2.c) + 1
            val right = max(i1.c, i2.c) - 1
            val top = min(i1.r, i2.r) + 1
            val bottom = max(i1.r, i2.r) - 1

            // no intersections with vertical edges while going from left to right
            val intersectsHorizontally = verticalEdges
                .filter { it.first.c in left .. right }
                .any { top in it.first.r .. it.second.r || bottom in it.first.r .. it.second.r }
            if (intersectsHorizontally) return false

            // no intersections with horizontal edges while going from top to bottom
            val intersectsVertically = horizontalEdges
                .filter { it.first.r in top .. bottom }
                .any { left in it.first.c .. it.second.c || right in it.first.c .. it.second.c }
            return !intersectsVertically
        }

        input.plus(input.first())
            .windowed(2) { (i1, i2) -> addEdge(i1, i2) }

        input.getAllPairs()
            .filter { (i1, i2) -> isValidRectangle(i1, i2) }
            .maxOf { (i1, i2) -> surfaceOfRectangle(i1, i2) }
    }

    val testInput = """
        7,1
        11,1
        11,7
        9,7
        9,5
        2,5
        2,3
        7,3
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 50
    }

    partTwoTest {
        testInput shouldOutput 24
    }
}
