package year2024.day12

import framework.solution
import util.common.CellIndex
import util.common.Dir
import util.common.algorithms.floodFill
import util.common.get

private typealias Input = List<List<Char>>

fun main() = solution(2024, 12) {

    fun countFencing(input: Input, perimeter: (Array<Array<Char>>, Char) -> Int): Int {
        val terrain = input.map { it.toTypedArray() }.toTypedArray()
        var totalFencingCost = 0
        while (true) {
            var next: Char? = null
            var nextIndex: CellIndex? = null

            for (r in terrain.indices) {
                for (c in terrain[r].indices) {
                    if (!terrain[r][c].isUpperCase()) continue
                    next = terrain[r][c]
                    nextIndex = CellIndex(r, c)
                    break
                }
            }

            if (next == null || nextIndex == null) break

            floodFill(terrain, nextIndex, Char::lowercaseChar)

            val target = next.lowercaseChar()
            val surface = terrain.sumOf { line -> line.count { it == target }}
            totalFencingCost += surface * perimeter(terrain, target)

            terrain.forEachIndexed { r, row ->
                row.forEachIndexed { c, ch ->
                    if (ch == target) terrain[r][c] = '*'
                }
            }
        }
        return totalFencingCost
    }

    parseInput { lines ->
        lines.map(String::toList)
    }

    partOne { input ->
        countFencing(input) { terrain, target ->
            var perimeter = 0
            for (r in terrain.indices) {
                for (c in terrain[r].indices) {
                    val index = CellIndex(r, c)
                    if (terrain[index] != target) continue
                    perimeter += index.neighbors
                        .count { it.isOutsideBoundsOf(terrain) ||  terrain[it] != target }
                }
            }
            perimeter
        }
    }

    partTwo { input ->
        data class Line(val index: CellIndex, val dir: Dir)

        fun countConsecutive(indices: List<Int>): Int =
            indices.mapIndexed { i, v ->
                if (i == 0 || v != indices[i-1] + 1) 1 else 0
            }.sum()

        countFencing(input) { terrain, target ->
            val verticalSides = mutableListOf<Line>()
            val horizontalSides = mutableListOf<Line>()
            for (r in terrain.indices) {
                for (c in terrain[r].indices) {
                    val index = CellIndex(r, c)
                    if (terrain[index] != target) continue
                    index.neighbors
                        .filter { it.isOutsideBoundsOf(terrain) || terrain[it] != target }
                        .forEach {
                            if (it.r == index.r)
                                verticalSides.add(
                                    Line(
                                        index = CellIndex(it.r, index.c),
                                        dir = if (it.c < index.c) Dir.L else Dir.R,
                                    ),
                                )
                            else
                                horizontalSides.add(
                                    Line(
                                        index = CellIndex(index.r, it.c),
                                        dir = if (it.r < index.r) Dir.U else Dir.D,
                                    ),
                                )
                        }
                }
            }
            val v = verticalSides
                .groupBy { it.index.c to it.dir }
                .map { (_, indices) -> indices.map { it.index.r } }
            val h = horizontalSides
                .groupBy { it.index.r to it.dir }
                .map { (_, indices) -> indices.map { it.index.c } }
            v.plus(h)
                .map(List<Int>::sorted)
                .sumOf(::countConsecutive)
        }
    }

    val testInput1 = """
        OOOOO
        OXOXO
        OOOOO
        OXOXO
        OOOOO
    """.trimIndent()

    val testInput2 = """
        RRRRIICCFF
        RRRRIICCCF
        VVRRRCCFFF
        VVRCCCJFFF
        VVVVCJJCFE
        VVIVCCJJEE
        VVIIICJJEE
        MIIIIIJJEE
        MIIISIJEEE
        MMMISSJEEE
    """.trimIndent()

    partOneTest {
        testInput1 shouldOutput 772
        testInput2 shouldOutput 1930
    }

    partTwoTest {
        """
            AAAA
            BBCD
            BBCC
            EEEC
        """.trimIndent() shouldOutput 80

        """
            EEEEE
            EXXXX
            EEEEE
            EXXXX
            EEEEE
        """.trimIndent() shouldOutput 236

        """
            AAAAAA
            AAABBA
            AAABBA
            ABBAAA
            ABBAAA
            AAAAAA
        """.trimIndent() shouldOutput 368

        testInput1 shouldOutput 436
        testInput2 shouldOutput 1206
    }
}
