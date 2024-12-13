package year2023.day18

import framework.solution
import util.common.CellIndex
import util.common.Dir
import util.common.algorithms.floodFill
import util.common.areaOfPolygon
import util.common.set

private data class DigInstruction(val dir: Dir, val amount: Int, val color: String)
private typealias Input = List<DigInstruction>

fun main() = solution<Input>(2023, 18) {

    parseInput { lines ->
        lines.map {  line ->
            val (dir, amount, color) = line.split(' ')
            DigInstruction(
                Dir.valueOf(dir),
                amount.toInt(),
                color.drop(2).dropLast(1)
            )
        }
    }

    partOne { input ->
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

        floodFill(terrain, CellIndex(0, 0)) { true }
        translatedIndices.count() + terrain.flatten().count { !it }
    }

    partTwo { input ->
        val modInput = input.map { it.color }.map {
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

        val paddedInput = listOf(modInput.last()).plus(modInput).plus(modInput.first())
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
                    else -> return@forEach
                }
            }

        outlineVertices.areaOfPolygon()
    }

    val testInput = """
        R 6 (#70c710)
        D 5 (#0dc571)
        L 2 (#5713f0)
        D 2 (#d2c081)
        R 2 (#59c680)
        D 2 (#411b91)
        L 5 (#8ceee2)
        U 2 (#caa173)
        L 1 (#1b58a2)
        U 2 (#caa171)
        R 2 (#7807d2)
        U 3 (#a77fa3)
        L 2 (#015232)
        U 2 (#7a21e3)
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 62
    }

    partTwoTest {
        testInput shouldOutput 952408144115L
    }
}
