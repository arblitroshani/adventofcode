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
        val edges = input
            .plus(input.first())
            .windowed(2)
            .map { (i1, i2) ->
                val start = CellIndex(min(i1.r, i2.r), min(i1.c, i2.c))
                val end = CellIndex(max(i1.r, i2.r), max(i1.c, i2.c))
                start to end
            }

        fun isValidRectangle(i1: CellIndex, i2: CellIndex): Boolean {
            val left = min(i1.c, i2.c) + 1
            val right = max(i1.c, i2.c) - 1
            val top = min(i1.r, i2.r) + 1
            val bottom = max(i1.r, i2.r) - 1

            return !edges.any { (e1, e2) ->
                val intersectsVerticalEdges = e1.c == e2.c &&
                        e1.c in left .. right &&
                        (top in e1.r .. e2.r || bottom in e1.r .. e2.r)
                val intersectsHorizontalEdges = e1.r == e2.r &&
                        e1.r in top .. bottom &&
                        (left in e1.c .. e2.c || right in e1.c .. e2.c)
                intersectsVerticalEdges || intersectsHorizontalEdges
            }
        }

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
