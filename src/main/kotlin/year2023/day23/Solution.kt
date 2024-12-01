package year2023.day23

import framework.solution
import util.common.CellIndex
import util.common.Dir
import util.common.GraphEdge
import util.common.get
import kotlin.math.min

private typealias Input = List<List<Char>>

fun main() = solution<Input>(2023, 23) {

    fun CellIndex.validNeighbors(input: Input): List<CellIndex> =
        neighbors.filterNot { it.isOutsideBoundsOf(input) || input[it] == '#' }

    parseInput { lines ->
        lines.map(String::toList)
    }

    partOne { input ->
        val start = CellIndex(0, 1)
        val target = CellIndex(input.size - 1, input[0].size - 2)

        fun longestDirectedPathToTarget(i: CellIndex, visited: List<CellIndex>): Int {
            if (i in visited) return 0
            if (i == target) return visited.size
            val nextIndexes = when (input[i]) {
                '>' -> listOf(i.right)
                'v' -> listOf(i.bottom)
                else -> i.validNeighbors(input)
            }
            return nextIndexes.maxOf { longestDirectedPathToTarget(it, visited.plus(i)) }
        }

        longestDirectedPathToTarget(i = start, visited = emptyList())
    }

    partTwo { input ->
        val vertices = mutableSetOf<CellIndex>()
        val edges = mutableMapOf<GraphEdge, Int>()
        val adjacencyList = mutableMapOf<CellIndex, MutableSet<Pair<CellIndex, Int>>>()
        val visitedVertices = mutableSetOf<Pair<CellIndex, Dir>>()

        val start = CellIndex(0, 1)
        val target = CellIndex(input.size - 1, input[0].size - 2)

        fun buildGraph(startVertex: CellIndex, dir: Dir) {
            if (Pair(startVertex, dir) in visitedVertices) return
            visitedVertices.add(Pair(startVertex, dir))

            var prev = startVertex
            var current = startVertex.next(dir)
            var cnt = 0

            while (true) {
                cnt++
                val neighbors = current.validNeighbors(input).filter { it != prev }
                if (neighbors.size != 1) {
                    vertices.addAll(listOf(startVertex, current))
                    val newEdge = GraphEdge(startVertex, current)
                    edges[newEdge] = min(cnt, edges[newEdge] ?: Int.MAX_VALUE)
                    neighbors.forEach { buildGraph(current, it.directionFrom(current)) }
                    break
                }
                prev = current
                current = neighbors[0]
            }
        }

        fun longestPathToTarget(i: CellIndex, visited: List<CellIndex>, actualCost: Int): Int {
            if (i in visited) return 0
            if (i == target) return actualCost
            return adjacencyList[i]!!
                .filterNot { it.first in visited }
                .maxOfOrNull { (neighbor, visitCost) ->
                    longestPathToTarget(neighbor, visited.plus(i), actualCost + visitCost)
                } ?: 0
        }

        vertices.clear()
        edges.clear()
        adjacencyList.clear()
        visitedVertices.clear()

        // step 1: build graph (edge list)
        buildGraph(start, Dir.D)

        // step 2: build adjacency list
        vertices.forEach { adjacencyList[it] = mutableSetOf() }
        edges.forEach { (edge, cost) ->
            adjacencyList[edge.start]?.add(Pair(edge.end, cost))
            adjacencyList[edge.end]?.add(Pair(edge.start, cost))
        }

        // step 3: backtrack all paths in graph
        longestPathToTarget(i = start, visited = emptyList(), actualCost = 0)
    }

    val testInput = """
        #.#####################
        #.......#########...###
        #######.#########.#.###
        ###.....#.>.>.###.#.###
        ###v#####.#v#.###.#.###
        ###.>...#.#.#.....#...#
        ###v###.#.#.#########.#
        ###...#.#.#.......#...#
        #####.#.#.#######.#.###
        #.....#.#.#.......#...#
        #.#####.#.#.#########v#
        #.#...#...#...###...>.#
        #.#.#v#######v###.###v#
        #...#.>.#...>.>.#.###.#
        #####v#.#.###v#.#.###.#
        #.....#...#...#.#.#...#
        #.#########.###.#.#.###
        #...###...#...#...#.###
        ###.###.#.###v#####v###
        #...#...#.#.>.>.#.>.###
        #.###.###.#.###.#.#v###
        #.....###...###...#...#
        #####################.#
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 94
    }

    partTwoTest {
        testInput shouldOutput 154
    }
}
