package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.AocPuzzle
import com.arblitroshani.adventofcode.util.common.UnionFind
import kotlin.math.max
import kotlin.math.min

typealias Input23d25 = MutableList<Pair<Int, Int>>

class Day25: AocPuzzle<Input23d25>() {

    override fun parseInput(lines: List<String>): Input23d25 {
        val stringNodes = lines.map { line ->
            val (source, targets) = line.split(": ")
            source to targets.split(' ')
        }

        val adjacencyList = stringNodes.map { it.first }
            .associateWith { mutableSetOf<String>() }
            .toMutableMap()

        stringNodes.forEach { (source, targets) ->
            targets.forEach {
                if (adjacencyList[source] == null) adjacencyList[source] = mutableSetOf()
                if (adjacencyList[it] == null) adjacencyList[it] = mutableSetOf()
                adjacencyList[source]?.add(it)
                adjacencyList[it]?.add(source)
            }
        }

        val intKeys = adjacencyList.keys
            .mapIndexed { index, s -> s to index }
            .toMap()

        val intAdjacencyList = adjacencyList.map { (k, v) ->
            intKeys[k]!! to v.map { intKeys[it]!! }.toSet()
        }.toMap()

        val edges = mutableSetOf<Pair<Int, Int>>()

        intAdjacencyList.forEach { (k, v) ->
            v.forEach { edges.add(Pair(min(k, it), max(k, it))) }
        }

        return edges.toMutableList()
    }

    override fun partOne(): Int {
        input.remove(Pair(61, 807))
        input.remove(Pair(735, 853))
        input.remove(Pair(883, 1092))

        val unionFind = UnionFind(input.maxOf { it.second } + 1)
        for ((x, y) in input) unionFind.union(x, y)

        val disjointSets = unionFind.getDisjointSets()
        if (disjointSets.size != 2) throw Exception("Incorrect solution!")
        return disjointSets[0].size * disjointSets[1].size
    }

    override fun partTwo(): Int = 0
}

fun main() = Day25().solve()
