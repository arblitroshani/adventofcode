package year2023.day10

import framework.solution
import util.common.CellIndex
import util.common.get
import kotlin.math.ceil

private typealias Input = List<List<Char>>

fun main() = solution<Input>(2023, 10) {

    val regexIntersections = Regex("(L-*7|F-*J|\\|)")

    fun stepsToGoal(
        input: Input,
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
                '7' -> if (prev.r == current.r) current.bottom else current.left
                'J' -> if (prev.r == current.r) current.top else current.left
                'L' -> if (prev.r == current.r) current.top else current.right
                'F' -> if (prev.r == current.r) current.bottom else current.right
                '|' -> if (prev.r < current.r) current.bottom else current.top
                '-' -> if (prev.c < current.c) current.right else current.left
                else -> CellIndex(-1, -1)
            }
            prev = steps.last()
        }
        return steps
    }

    fun getLoop(input: Input): List<CellIndex> {
        val targetX = input.indexOfFirst { it.contains('S') }
        val targetY = input[targetX].indexOf('S')
        val loopWithoutS = stepsToGoal(
            input,
            startCell = CellIndex(targetX - 1, targetY), // TODO: remove hardcoded -1: find adjacent start index
            previousCell = CellIndex(targetX, targetY),
            target = CellIndex(targetX, targetY),
        )
        return loopWithoutS
            .toMutableList()
            .also { it.add(CellIndex(targetX, targetY)) }
    }

    fun isInside(input: Input, index: CellIndex, loop: List<CellIndex>): Boolean {
        if (loop.contains(index)) return false
        val stringToRight = (index.c + 1 ..< input[0].size).map {
            val cell = input[index.r][it]
            if (cell in "-|FJ7L" && !loop.contains(CellIndex(index.r, it))) "a" else cell
        }.joinToString(separator = "")
        return regexIntersections.findAll(stringToRight).count() % 2 == 1
    }

    parseInput { lines ->
        val tiles = lines
            .map(String::toMutableList)
            .map { line ->
                line.apply {
                    add(0, '.')
                    add(line.size, '.')
                }
            }

        val horizontalPad = List(tiles[0].size) { '.' }.toMutableList()

        tiles.toMutableList().apply {
            add(tiles.size, horizontalPad)
            add(0, horizontalPad)
        }
    }

    partOne { input ->
        ceil(getLoop(input).size / 2.0).toInt()
    }

    partTwo { input ->
        val loop = getLoop(input)
        val startCell = loop.last()

        val modifiedInput = input.mapIndexed { i, chars ->
            if (i == startCell.r)
                chars.mapIndexed { j, char ->
                    // TODO: remove hardcoded L: replace S with appropriate connecting pipe
                    if (j == startCell.c) 'L' else char
                }
            else
                chars
        }

        input.mapIndexed { i, chars ->
            List(chars.size) { j -> CellIndex(i, j) }
                .count { isInside(modifiedInput, it, loop) }
        }.sum()
    }
}
