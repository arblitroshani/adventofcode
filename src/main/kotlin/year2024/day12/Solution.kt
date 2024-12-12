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
        var sum = 0

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
            sum += surface * perimeter(terrain, target)

            terrain.forEachIndexed { r, row ->
                row.forEachIndexed { c, ch ->
                    if (ch == target) terrain[r][c] = '*'
                }
            }
        }
        return sum
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
                    perimeter += 4 - index.neighbors
                        .filter { it.isInsideBoundsOf(terrain) }
                        .count { terrain[it] == target }
                }
            }
            perimeter
        }
    }

    partTwo { input ->
        data class Line(val index: CellIndex, val dir: Dir)

        fun countConsecutive(indices: List<Int>): Int {
            val sortedIndices = indices.sorted()
            return sortedIndices.mapIndexed { i, v ->
                if (i == 0 || v != sortedIndices[i-1] + 1) 1 else 0
            }.sum()
        }

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
                            if (it.x == index.x) {
                                if (it.y < index.y) {
                                    verticalSides.add(Line(CellIndex(it.x, index.y), Dir.L))
                                } else {
                                    verticalSides.add(Line(CellIndex(it.x, index.y + 1), Dir.R))
                                }
                            } else {
                                if (it.x < index.x) {
                                    horizontalSides.add(Line(CellIndex(index.x, it.y), Dir.U))
                                } else {
                                    horizontalSides.add(Line(CellIndex(index.x + 1, it.y), Dir.D))
                                }
                            }
                        }
                }
            }
            val v = verticalSides
                .groupBy { it.index.y to it.dir }
                .map { (_, indices) -> indices.map { it.index.x } }
                .sumOf(::countConsecutive)
            val h = horizontalSides
                .groupBy { it.index.x to it.dir }
                .map { (_, indices) -> indices.map { it.index.y } }
                .sumOf(::countConsecutive)
            v + h
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
