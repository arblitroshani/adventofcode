package year2024.day04

import framework.solution
import util.common.CellIndex
import util.common.Dir

private typealias Input = List<List<Char>>

fun main() = solution<Input>(2024, 4) {

    operator fun Input.get(ci: CellIndex) = this[ci.x][ci.y]

    parseInput { lines ->
        lines.map(String::toList)
    }

    partOne { input ->
        input.indices.sumOf { r ->
            input[r].indices.sumOf { c ->
                Dir.entries.count { dir ->
                    var cellIndex = CellIndex(r, c)
                    "XMAS".forEach { ch ->
                        if (cellIndex.isOutsideBoundsOf(input)) return@count false
                        if (input[cellIndex] != ch) return@count false
                        cellIndex = cellIndex.next(dir)
                    }
                    true
                }
            }
        }
    }

    partTwo { input ->
        val target = listOf('M', 'S', 'S', 'M')
        val rotations = target.indices.map { target.drop(it) + target.take(it) }

        input.indices.sumOf { r ->
            input[r].indices
                .filter { input[r][it] == 'A' }
                .map { CellIndex(r, it).diagonalNeighbors }
                .count { dn ->
                    dn.all { !it.isOutsideBoundsOf(input) } && dn.map { input[it] } in rotations
                }
        }
    }

    val testInput = """
        MMMSXXMASM
        MSAMXMSMSA
        AMXSXMAAMM
        MSAMASMSMX
        XMASAMXAMM
        XXAMMXXAMA
        SMSMSASXSS
        SAXAMASAAA
        MAMMMXMMMM
        MXMXAXMASX
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 18
    }

    partTwoTest {
        testInput shouldOutput 9
    }
}
