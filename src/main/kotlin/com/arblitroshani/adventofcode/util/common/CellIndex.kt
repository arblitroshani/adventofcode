package com.arblitroshani.adventofcode.util.common

import kotlin.math.abs

data class CellIndex(val x: Int, val y: Int) {
    val top: CellIndex get() = CellIndex(x - 1, y)
    val right: CellIndex get() = CellIndex(x, y + 1)
    val bottom: CellIndex get() = CellIndex(x + 1, y)
    val left: CellIndex get() = CellIndex(x, y - 1)

    fun manhattanDistance(to: CellIndex) = abs(x - to.x) + abs(y - to.y)
}

operator fun <T> List<List<T>>.get(index: CellIndex): T { return this[index.x][index.y] }
operator fun <T> List<MutableList<T>>.set(index: CellIndex, value: T) { this[index.x][index.y] = value }
