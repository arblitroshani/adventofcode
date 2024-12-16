package year2023.day17

import framework.solution
import util.common.CellIndex
import util.common.Dir
import util.common.algorithms.dijkstra
import util.common.get

private data class Input(val grid: List<List<Int>>, val start: CellIndex, val target: CellIndex)

fun main() = solution<Input>(2023, 17) {

    data class Node(val index: CellIndex, val dir: Dir, val stepsDoneInCurrentDir: Int)

    parseInput { lines ->
        Input(
            grid = lines.map { line ->
                line.toList().map { it - '0' }
            },
            start = CellIndex(0, 0),
            target = CellIndex(lines.size - 1, lines[0].length - 1)
        )
    }

    fun minHeatLoss(
        input: Input,
        adjacency: (Node) -> List<Node>,
        isGoal: (Node) -> Boolean,
    ) : Long {
        val path = dijkstra(
            start = Node(input.start, Dir.R, 0),
            adjacency = {
                val nextNodes = if (it.index == input.start) {
                    listOf(
                        Node(input.start.right, Dir.R, 1),
                        Node(input.start.bottom, Dir.D, 1),
                    )
                } else {
                    adjacency(it)
                }
                nextNodes
                    .filter { t -> t.index.isInsideBoundsOf(input.grid) }
                    .map { t -> t to input.grid[t.index].toLong() }
            },
            isGoal = isGoal,
        )
        return path.sumOf { it.second }
    }

    partOne { input ->
        minHeatLoss(
            input = input,
            adjacency = {
                buildList {
                    if (it.stepsDoneInCurrentDir < 3) {
                        add(Node(it.index.next(it.dir), it.dir, it.stepsDoneInCurrentDir + 1))
                    }
                    add(Node(it.index.next(it.dir.cw), it.dir.cw, 1))
                    add(Node(it.index.next(it.dir.ccw), it.dir.ccw, 1))
                }
            },
            isGoal = { it.index == input.target }
        )
    }

    partTwo { input ->
        minHeatLoss(
            input = input,
            adjacency = {
                buildList {
                    if (it.stepsDoneInCurrentDir < 4) {
                        add(Node(it.index.next(it.dir), it.dir, it.stepsDoneInCurrentDir + 1))
                    } else {
                        if (it.stepsDoneInCurrentDir < 10) {
                            add(Node(it.index.next(it.dir), it.dir, it.stepsDoneInCurrentDir + 1))
                        }
                        add(Node(it.index.next(it.dir.cw), it.dir.cw, 1))
                        add(Node(it.index.next(it.dir.ccw), it.dir.ccw, 1))
                    }
                }
            },
            isGoal = { it.index == input.target && it.stepsDoneInCurrentDir >= 4 }
        )
    }

    val testInput = """
        2413432311323
        3215453535623
        3255245654254
        3446585845452
        4546657867536
        1438598798454
        4457876987766
        3637877979653
        4654967986887
        4564679986453
        1224686865563
        2546548887735
        4322674655533
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 102
    }

    partTwoTest {
        testInput shouldOutput 94

        """
            111111111111
            999999999991
            999999999991
            999999999991
            999999999991
        """.trimIndent() shouldOutput 71
    }
}
