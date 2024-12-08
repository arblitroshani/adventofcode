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

    fun numberOfInterferenceCells(input: Input, limit: Int, countSelf: Boolean): Int =
        input.antennas
            .flatMap { it.value.getAllPairs() }
            .flatMap { (a, b) ->
                val diff = a - b
                listOf(
                    generateSequence(a) { it + diff },
                    generateSequence(b) { it - diff },
                )
            }
            .flatMap { sequences ->
                sequences
                    .takeWhile { it.isInsideBoundsOf(input.grid) }
                    .drop(if (countSelf) 0 else 1)
                    .take(limit)
            }
            .distinct()
            .size

    partOne { input ->
        numberOfInterferenceCells(input, limit = 1, countSelf = false)
    }

    partTwo { input ->
        numberOfInterferenceCells(input, limit = input.grid.size, countSelf = true)
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
