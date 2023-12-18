package com.arblitroshani.adventofcode.util.common

import kotlin.math.abs

data class CellIndex(val x: Int, val y: Int) {
    val top: CellIndex get() = CellIndex(x - 1, y)
    val right: CellIndex get() = CellIndex(x, y + 1)
    val bottom: CellIndex get() = CellIndex(x + 1, y)
    val left: CellIndex get() = CellIndex(x, y - 1)

    fun isOutsideBoundsOf(grid: List<List<Any>>): Boolean =
        x < 0 || y < 0 || x >= grid.size || y >= grid[0].size

    fun next(d: Dir): CellIndex = when (d) {
        Dir.U -> top
        Dir.D -> bottom
        Dir.L -> left
        Dir.R -> right
    }

    fun manhattanDistance(to: CellIndex) = abs(x - to.x) + abs(y - to.y)
}

operator fun <T> List<List<T>>.get(index: CellIndex): T { return this[index.x][index.y] }
operator fun <T> List<MutableList<T>>.set(index: CellIndex, value: T) { this[index.x][index.y] = value }

fun List<CellIndex>.areaOfPolygon(): Long {
    var area = 0L
    var j = size - 1
    for (i in indices) {
        area += (this[j].x + this[i].x).toLong() * (this[j].y - this[i].y).toLong()
        j = i
    }
    return abs(area / 2)
}
