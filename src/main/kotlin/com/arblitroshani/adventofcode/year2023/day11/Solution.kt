package com.arblitroshani.adventofcode.year2023.day11

import com.arblitroshani.adventofcode.framework.solution
import com.arblitroshani.adventofcode.util.common.CellIndex
import kotlin.math.max
import kotlin.math.min

private typealias Input = List<List<Char>>

fun main() = solution<Input>(2023, 11) {

    fun countDistances(input: Input, expansion: Int): Long {
        val emptyRows = input
            .mapIndexed { i, line -> if (!line.contains('#')) i else -1 }
            .filter { it > 0 }
        val emptyCols = (0 until input[0].size)
            .map { i -> i to input.map { it[i] } }
            .filterNot { it.second.contains('#') }
            .map { it.first }
        val galaxies = mutableListOf<CellIndex>()

        input.forEachIndexed { i, line ->
            line.forEachIndexed { j, c -> if (c == '#') galaxies.add(CellIndex(i, j)) }
        }

        var sum = 0L
        for (i in 0 .. galaxies.size)
            for (j in i + 1 ..< galaxies.size) {
                val crossedEmptyLines = emptyRows.count {
                    it in min(galaxies[i].x, galaxies[j].x) + 1 until max(galaxies[i].x, galaxies[j].x)
                }
                val crossedEmptyCols = emptyCols.count {
                    it in min(galaxies[i].y, galaxies[j].y) + 1 until max(galaxies[i].y, galaxies[j].y)
                }
                sum += galaxies[i].manhattanDistance(to = galaxies[j])
                sum += (expansion - 1) * (crossedEmptyLines + crossedEmptyCols)
            }
        return sum
    }

    parseInput { lines ->
        lines.map(String::toMutableList).toMutableList()
    }

    partOne { input ->
        countDistances(input, 2)
    }

    partTwo { input ->
        countDistances(input, 1000000)
    }

    val testInput = """
        ...#......
        .......#..
        #.........
        ..........
        ......#...
        .#........
        .........#
        ..........
        .......#..
        #...#.....
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 374
    }

    partTwoTest {
        testInput with {
            countDistances(it, 10)
        } shouldOutput 1030

        testInput with {
            countDistances(it, 100)
        } shouldOutput 8410
    }
}
