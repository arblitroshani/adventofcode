package year2024.day16

import framework.solution
import util.common.CellIndex
import util.common.Dir
import util.common.algorithms.dijkstraExhaustive
import util.common.get

private data class Input(val grid: List<CharArray>, val start: CellIndex, val end: CellIndex)

fun main() = solution<Input>(2024, 16) {

    data class Node(val index: CellIndex, val dir: Dir)

    fun paths(input: Input): List<List<Pair<Node, Long>>> =
        dijkstraExhaustive(
            start = Node(input.start, Dir.R),
            adjacency = { node ->
                node.dir
                    .let { listOf(it to 0, it.cw to 1000, it.ccw to 1000) }
                    .map { (dir, cost) -> Node(node.index.next(dir), dir) to cost.toLong() + 1 }
                    .filterNot { (node, _) -> input.grid[node.index] == '#' }
            },
            isGoal = { it.index == input.end },
        )

    parseInput { lines ->
        Input(
            grid = lines.map(String::toCharArray),
            start = CellIndex(lines.count() - 2, 1),
            end = CellIndex(1, lines[1].count() - 2),
        )
    }

    partOne { input ->
        paths(input)
            .first()
            .sumOf { it.second }
    }

    partTwo { input ->
        paths(input)
            .flatten()
            .map { it.first.index }
            .distinct()
            .count()
    }

    val testInput1 = """
        ###############
        #.......#....E#
        #.#.###.#.###.#
        #.....#.#...#.#
        #.###.#####.#.#
        #.#.#.......#.#
        #.#.#####.###.#
        #...........#.#
        ###.#.#####.#.#
        #...#.....#.#.#
        #.#.#.###.#.#.#
        #.....#...#.#.#
        #.###.#.#.#.#.#
        #S..#.....#...#
        ###############
    """.trimIndent()

    val testInput2 = """
        #################
        #...#...#...#..E#
        #.#.#.#.#.#.#.#.#
        #.#.#.#...#...#.#
        #.#.#.#.###.#.#.#
        #...#.#.#.....#.#
        #.#.#.#.#.#####.#
        #.#...#.#.#.....#
        #.#.#####.#.###.#
        #.#.#.......#...#
        #.#.###.#####.###
        #.#.#...#.....#.#
        #.#.#.#####.###.#
        #.#.#.........#.#
        #.#.#.#########.#
        #S#.............#
        #################
    """.trimIndent()

    partOneTest {
        testInput1 shouldOutput 7036L
        testInput2 shouldOutput 11048L
    }

    partTwoTest {
        testInput1 shouldOutput 45
        testInput2 shouldOutput 64
    }
}
