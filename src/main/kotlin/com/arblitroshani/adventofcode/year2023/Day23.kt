package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.AocPuzzle
import com.arblitroshani.adventofcode.util.common.CellIndex
import com.arblitroshani.adventofcode.util.common.Dir
import com.arblitroshani.adventofcode.util.common.GraphEdge
import com.arblitroshani.adventofcode.util.common.get
import kotlin.collections.set
import kotlin.math.min

typealias Input23d23 = List<List<Char>>

class Day23: AocPuzzle<Input23d23>() {

    private lateinit var start: CellIndex
    private lateinit var target: CellIndex

    override fun parseInput(lines: List<String>): Input23d23 {
        start = CellIndex(0, 1)
        target = CellIndex(lines.size - 1, lines[0].length - 2)
        return lines.map(String::toList)
    }

    // MARK: - Part 1

    override fun partOne(): Int =
        longestDirectedPathToTarget(i = start, visited = emptyList())

    private fun longestDirectedPathToTarget(i: CellIndex, visited: List<CellIndex>): Int {
        if (i in visited) return 0
        if (i == target) return visited.size
        val nextIndexes = when (input[i]) {
            '>' -> listOf(i.right)
            'v' -> listOf(i.bottom)
            else -> i.validNeighbors
        }
        return nextIndexes.maxOf { longestDirectedPathToTarget(it, visited.plus(i)) }
    }

    // MARK: - Part 2

    private var vertices = mutableSetOf<CellIndex>()
    private val edges = mutableMapOf<GraphEdge, Int>()
    private val adjacencyList = mutableMapOf<CellIndex, MutableSet<Pair<CellIndex, Int>>>()

    private val visitedVertices = mutableSetOf<Pair<CellIndex, Dir>>()

    private val CellIndex.validNeighbors: List<CellIndex>
        get() = neighbors.filterNot { it.isOutsideBoundsOf(input) || input[it] == '#' }

    override fun partTwo(): Int {
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
        return longestPathToTarget(i = start, visited = emptyList(), actualCost = 0)
    }

    private fun buildGraph(startVertex: CellIndex, dir: Dir) {
        if (Pair(startVertex, dir) in visitedVertices) return
        visitedVertices.add(Pair(startVertex, dir))

        var prev = startVertex
        var current = startVertex.next(dir)
        var cnt = 0

        while (true) {
            cnt++
            val neighbors = current.validNeighbors.filter { it != prev }
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

    private fun longestPathToTarget(i: CellIndex, visited: List<CellIndex>, actualCost: Int): Int {
        if (i in visited) return 0
        if (i == target) return actualCost
        return adjacencyList[i]!!
            .filterNot { it.first in visited }
            .maxOfOrNull { (neighbor, visitCost) ->
                longestPathToTarget(neighbor, visited.plus(i), actualCost + visitCost)
            } ?: 0
    }
}

fun main() = Day23().solve(
    expectedAnswerForSampleInP1 = 94,
    expectedAnswerForSampleInP2 = 154,
)
