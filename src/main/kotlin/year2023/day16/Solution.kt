package year2023.day16

import framework.solution
import util.common.CellIndex
import util.common.Dir
import util.common.get
import util.common.set

private typealias Input = List<List<Char>>

fun main() = solution<Input>(2023, 16) {

    lateinit var visited: List<MutableList<Boolean>>
    val memo = hashMapOf<Pair<CellIndex, Dir>, Boolean>()

    fun visitTile(input: Input, prevIndex: CellIndex, d: Dir) {
        val i = prevIndex.next(d)
        if (i.isOutsideBoundsOf(input)) return

        val key = Pair(i, d)
        if (memo[key] == true) return

        memo[key] = true
        visited[i] = true

        when (input[i]) {
            if (d.isVertical) '/' else '\\' -> visitTile(input, i, d.cw)
            if (d.isVertical) '\\' else '/' -> visitTile(input, i, d.ccw)
            if (d.isVertical) '-' else '|' -> { visitTile(input, i, d.ccw); visitTile(input, i, d.cw) }
            else -> visitTile(input, i, d)
        }
    }

    fun countEnergizedTiles(input: Input, x: Int, y: Int, d: Dir): Int {
        visited = List(input.size) { MutableList(input[0].size) { false } }
        memo.clear()
        visitTile(input, CellIndex(x, y), d)
        return visited.flatten().count { it }
    }

    parseInput { lines ->
        lines.map(String::toList)
    }

    partOne { input ->
        countEnergizedTiles(input, 0, -1, Dir.R)
    }

    partTwo { input ->
        val rowIndices = input.first().indices
        val columnIndices = input.indices
        maxOf(
            rowIndices.maxOf { countEnergizedTiles(input, -1, it, Dir.D) },
            rowIndices.maxOf { countEnergizedTiles(input, input.size, it, Dir.U) },
            columnIndices.maxOf { countEnergizedTiles(input, it, -1, Dir.R) },
            columnIndices.maxOf { countEnergizedTiles(input, it, input[0].size, Dir.L) },
        )
    }

    val testInput = """
        .|...\....
        |.-.\.....
        .....|-...
        ........|.
        ..........
        .........\
        ..../.\\..
        .-.-/..|..
        .|....-|.\
        ..//.|....
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 46
    }

    partTwoTest {
        testInput shouldOutput 51
    }
}
