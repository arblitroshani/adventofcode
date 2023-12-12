package com.arblitroshani.adventofcode.util.common

import kotlin.math.abs

data class CellIndex(val x: Int, val y: Int) {
    val top: CellIndex get() = CellIndex(x - 1, y)
    val right: CellIndex get() = CellIndex(x, y + 1)
    val bottom: CellIndex get() = CellIndex(x + 1, y)
    val left: CellIndex get() = CellIndex(x, y - 1)

    fun manhattanDistance(to: CellIndex) = abs(x - to.x) + abs(y - to.y)
}
