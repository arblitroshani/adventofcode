package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.AocPuzzle
import com.arblitroshani.adventofcode.util.common.CellIndex
import com.arblitroshani.adventofcode.util.common.Dir
import com.arblitroshani.adventofcode.util.common.areaOfPolygon
import com.arblitroshani.adventofcode.util.common.get
import com.arblitroshani.adventofcode.util.common.set
import java.util.LinkedList
import java.util.Queue

data class DigInstruction(val dir: Dir, val amount: Int, val color: String)
typealias Input23d18 = List<DigInstruction>

class Day18: AocPuzzle<Input23d18>() {

    override fun parseInput(lines: List<String>): Input23d18 =
        lines.map {  line ->
            val (dir, amount, color) = line.split(' ')
            DigInstruction(Dir.valueOf(dir), amount.toInt(), color.drop(2).dropLast(1))
        }

    override fun partOne(): Int {
        var currentIndex = CellIndex(0, 0)
        val indices = mutableSetOf(currentIndex)
        input.forEach {
            (0 ..< it.amount).forEach { _ ->
                currentIndex = currentIndex.next(it.dir)
                indices.add(currentIndex)
            }
        }

        val minX = indices.minBy { it.x }.x
        val minY = indices.minBy { it.y }.y
        val translatedIndices = indices.map { CellIndex(it.x - minX, it.y - minY) }
        val height = translatedIndices.maxBy { it.x }.x + 1
        val width = translatedIndices.maxBy { it.y }.y + 1

        val terrain = Array(height + 2) { Array(width + 2) { false } }
        translatedIndices.forEach { terrain[it.right.bottom] = true }

        floodFill(terrain, CellIndex(0, 0))
        return translatedIndices.count() + terrain.flatten().count { !it }
    }

    private fun floodFill(terrain: Array<Array<Boolean>>, index: CellIndex) {
        val q: Queue<CellIndex> = LinkedList()
        val visited = Array(terrain.size) { Array(terrain[0].size) { false } }
        val color = terrain[index]
        q.offer(index)
        while (q.isNotEmpty()) {
            val current = q.poll()
            if (visited[current] || terrain[current] != color) { continue }
            visited[current] = true
            terrain[current] = true
            current.neighbors
                .filterNot { it.isOutsideBoundsOf(terrain) }
                .forEach(q::add)
        }
    }

    override fun partTwo(): Long {
        val input = input.map { it.color }.map {
            val amount = it.take(5).toInt(radix = 16)
            val dir = when (it.last()) {
                '0' -> Dir.R
                '1' -> Dir.D
                '2' -> Dir.L
                else -> Dir.U
            }
            DigInstruction(dir, amount, "")
        }

        val outlineVertices = mutableListOf<CellIndex>()
        var currentIndex = CellIndex(0, 0)

        val paddedInput = listOf(input.last()).plus(input).plus(input.first())
        paddedInput
            .windowed(3)
            .map { (prev, curr, next) ->
                val twoCwTurns = curr.dir == prev.dir.cw && next.dir == curr.dir.cw
                val twoCcwTurns = curr.dir == prev.dir.ccw && next.dir == curr.dir.ccw
                val delta = if (twoCwTurns) 1 else if (twoCcwTurns) -1 else 0
                curr.dir to curr.amount + delta
            }
            .forEach { (dir, amount) ->
                outlineVertices.add(currentIndex)
                currentIndex = when (dir) {
                    Dir.R -> currentIndex.copy(x = currentIndex.x + amount)
                    Dir.L -> currentIndex.copy(x = currentIndex.x - amount)
                    Dir.U -> currentIndex.copy(y = currentIndex.y + amount)
                    Dir.D -> currentIndex.copy(y = currentIndex.y - amount)
                }
            }

        return outlineVertices.areaOfPolygon()
    }
}

fun main() = Day18().solve(
    expectedAnswerForSampleInP1 = 62,
    expectedAnswerForSampleInP2 = 952408144115L,
)
