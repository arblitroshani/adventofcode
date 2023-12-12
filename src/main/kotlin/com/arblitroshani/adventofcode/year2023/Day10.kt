package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.AocPuzzle
import com.arblitroshani.adventofcode.util.common.CellIndex
import kotlin.math.ceil

fun main() = Day10().solve(
    expectedAnswerForSampleInP1 = null,
    expectedAnswerForSampleInP2 = null,
)

private typealias Input23d10 = List<List<Char>>

operator fun Input23d10.get(index: CellIndex): Char { return this[index.x][index.y] }

private class Day10: AocPuzzle<Input23d10>() {

    private val regexIntersections = Regex("(L-*7|F-*J|\\|)")

    override fun parseInput(input: List<String>): Input23d10 {
        val tiles = input
            .map(String::toMutableList)
            .map { line ->
                line.also {
                    it.add(0, '.')
                    it.add(line.size, '.')
                }
            }

        val horizontalPad = List(tiles[0].size) { '.' }.toMutableList()

        return tiles.toMutableList().also {
            it.add(tiles.size, horizontalPad)
            it.add(0, horizontalPad)
        }
    }

    override fun partOne(input: Input23d10): Int {
        val loop = getLoop(input)
        return ceil(loop.size / 2.0).toInt()
    }

    override fun partTwo(input: Input23d10): Int {
        val loop = getLoop(input)
        val startCell = loop.last()

        val modifiedInput = input.mapIndexed { i, chars ->
            if (i == startCell.x)
                chars.mapIndexed { j, char ->
                    // TODO: remove hardcoded L: replace S with appropriate connecting pipe
                    if (j == startCell.y) 'L' else char
                }
            else
                chars
        }

        return input
            .mapIndexed { i, chars ->
                List(chars.size) { j -> CellIndex(i, j) }
                    .count { isInside(modifiedInput, it, loop) }
            }.sum()
    }

    private fun getLoop(input: Input23d10): List<CellIndex> {
        val targetX = input.indexOfFirst { it.contains('S') }
        val targetY = input[targetX].indexOf('S')
        val loopWithoutS = stepsToGoal(
            input,
            startCell = CellIndex(targetX - 1, targetY), // TODO: remove hardcoded -1: find adjacent start index
            previousCell = CellIndex(targetX, targetY),
            target = CellIndex(targetX, targetY),
        )
        return loopWithoutS.toMutableList().also { it.add(CellIndex(targetX, targetY)) }
    }

    private fun isInside(input: Input23d10, index: CellIndex, loop: List<CellIndex>): Boolean {
        if (loop.contains(index)) return false
        val stringToRight = (index.y + 1 ..< input[0].size).map {
            val cell = input[index.x][it]
            if (cell in "-|FJ7L" && !loop.contains(CellIndex(index.x, it))) "a" else cell
        }.joinToString(separator = "")
        return regexIntersections.findAll(stringToRight).count() % 2 == 1
    }

    private fun stepsToGoal(
        input: Input23d10,
        startCell: CellIndex,
        previousCell: CellIndex,
        target: CellIndex,
    ): List<CellIndex> {
        val steps = mutableListOf<CellIndex>()
        var prev = previousCell
        var current = startCell

        while (current != target) {
            steps.add(current)
            current = when(input[current]) {
                '7' -> if (prev.x == current.x) current.bottom else current.left
                'J' -> if (prev.x == current.x) current.top else current.left
                'L' -> if (prev.x == current.x) current.top else current.right
                'F' -> if (prev.x == current.x) current.bottom else current.right
                '|' -> if (prev.x < current.x) current.bottom else current.top
                '-' -> if (prev.y < current.y) current.right else current.left
                else -> CellIndex(-1, -1)
            }
            prev = steps.last()
        }
        return steps
    }
}
