package year2024.day06

import framework.solution
import util.common.CellIndex
import util.common.Dir

private data class Input(
    val grid: List<List<Char>>,
    val obstructions: Set<CellIndex>,
    val startPosition: CellIndex,
)

fun main() = solution<Input>(2024, 6) {

    fun traverse(input: Input, onVisit: (Pair<CellIndex, Dir>) -> Boolean) {
        var pos = input.startPosition
        var dir = Dir.U
        while(pos.isInsideBoundsOf(input.grid)) {
            val next = pos.next(dir)
            if (next in input.obstructions) {
                dir = dir.cw
            } else {
                val shouldContinue = onVisit(next to dir)
                if (!shouldContinue) break
                pos = next
            }
        }
    }

    parseInput { lines ->
        val obstructions = mutableSetOf<CellIndex>()
        var guardStartPosition = CellIndex(0, 0)
        lines.forEachIndexed { x, row ->
            row
                .mapIndexed { y, cell -> CellIndex(x, y) to cell }
                .forEach { (index, cell) ->
                    when (cell) {
                        '^' -> guardStartPosition = index
                        '#' -> obstructions.add(index)
                    }
                }
        }
        Input(
            obstructions = obstructions,
            startPosition = guardStartPosition,
            grid = lines.map(String::toList),
        )
    }

    partOne { input ->
        val visited = mutableSetOf(input.startPosition)
        traverse(input) { (next, _) -> visited.add(next); true }
        visited.count() - 1
    }

    partTwo { (grid, obstructions, startPosition) ->
        var cnt = 0
        grid.indices.forEach { i ->
            grid[i].indices
                .map { j -> CellIndex(i, j) }
                .filterNot { it in obstructions || it == startPosition }
                .forEach { index ->
                    val visited = mutableSetOf<Pair<CellIndex, Dir>>()
                    traverse(Input(grid, obstructions.plus(index), startPosition)) { nextPair ->
                        if (nextPair in visited) { cnt++; false }
                        else { visited.add(nextPair); true }
                    }
                }
        }
        cnt
    }

    val testInput = """
        ....#.....
        .........#
        ..........
        ..#.......
        .......#..
        ..........
        .#..^.....
        ........#.
        #.........
        ......#...
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 41
    }

    partTwoTest {
        testInput shouldOutput 6
    }
}
