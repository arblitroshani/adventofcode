package year2024.day08

import framework.solution
import util.common.CellIndex
import util.extension.getAllPairs

private data class Input(
    val grid: List<List<Char>>,
    val antennas: Map<Char, List<CellIndex>>,
)

fun main() = solution<Input>(2024, 8) {

    parseInput { lines ->
        Input(
            grid = lines.map(String::toList),
            antennas = lines
                .flatMapIndexed { r, row ->
                    row.mapIndexedNotNull { c, cell ->
                        if (cell == '.') null else cell to CellIndex(r, c)
                    }
                }
                .groupBy({ it.first }, { it.second }),
        )
    }

    fun interferenceCells(input: Input, limit: Int, countSelf: Boolean): Set<CellIndex> =
        input.antennas
            .flatMap { it.value.getAllPairs() }
            .flatMap { (a, b) ->
                val left = if (a.y < b.y) a else b
                val right = if (left == a) b else a
                val xDiff = left.x - right.x
                val yDiff = left.y - right.y
                listOf(
                    generateSequence(left) { CellIndex(it.x + xDiff, it.y + yDiff) },
                    generateSequence(right) { CellIndex(it.x - xDiff, it.y - yDiff) },
                )
            }
            .flatMap { sequences ->
                sequences
                    .takeWhile { it.isInsideBoundsOf(input.grid) }
                    .drop(if (countSelf) 0 else 1)
                    .take(limit)
            }
            .toSet()

    partOne { input ->
        interferenceCells(input, limit = 1, countSelf = false)
            .size
    }

    partTwo { input ->
        interferenceCells(input, limit = input.grid.size, countSelf = true)
            .size
    }

    val mainTestInput = """
        ............
        ........0...
        .....0......
        .......0....
        ....0.......
        ......A.....
        ............
        ............
        ........A...
        .........A..
        ............
        ............
    """.trimIndent()

    partOneTest {
        """
            ..........
            ..........
            ..........
            ....a.....
            ........a.
            .....a....
            ..........
            ..........
            ..........
            ..........
        """.trimIndent() shouldOutput 4

        mainTestInput shouldOutput 14
    }

    partTwoTest {
        """
            T.........
            ...T......
            .T........
            ..........
            ..........
            ..........
            ..........
            ..........
            ..........
            ..........
        """.trimIndent() shouldOutput 9

        mainTestInput shouldOutput 34
    }
}
