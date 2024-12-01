package year2023.day21

import framework.solution
import util.common.CellIndex
import util.common.get

private typealias Input = List<List<Char>>

fun main() = solution<Input>(2023, 21) {

    val memo = mutableMapOf<Pair<CellIndex, Int>, Set<CellIndex>>()

    fun step(input: Input, index: CellIndex, remaining: Int): Set<CellIndex> {
        if (remaining < 0 || input[index] == '#') return emptySet()
        if (remaining == 0) return setOf(index)

        val memoKey = Pair(index, remaining)
        if (memo[memoKey] == null)
            memo[memoKey] = index.neighbors.fold(emptySet()) { acc, n ->
                acc + step(input, n, remaining - 1)
            }
        return memo[memoKey]!!
    }

    fun solve(input: Input, steps: Int): Int {
        val startIndex = CellIndex(input.size / 2, input[0].size / 2)
        return step(input, startIndex, steps).count()
    }

    parseInput { lines ->
        lines.map(String::toList)
    }

    partOne { input ->
        solve(input, 64)
    }

    partOneTest {
        """
            ...........
            .....###.#.
            .###.##..#.
            ..#.#...#..
            ....#.#....
            .##..S####.
            .##..#...#.
            .......##..
            .##.#.####.
            .##..##.##.
            ...........
        """.trimIndent() with {
            solve(it, 6)
        } shouldOutput 16
    }
}
