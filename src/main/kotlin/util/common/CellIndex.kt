package util.common

import kotlin.math.abs

data class CellIndex(val x: Int, val y: Int) {
    val top: CellIndex get() = CellIndex(x - 1, y)
    val right: CellIndex get() = CellIndex(x, y + 1)
    val bottom: CellIndex get() = CellIndex(x + 1, y)
    val left: CellIndex get() = CellIndex(x, y - 1)

    val topRight: CellIndex get() = CellIndex(x - 1, y + 1)
    val bottomRight: CellIndex get() = CellIndex(x + 1, y + 1)
    val bottomLeft: CellIndex get() = CellIndex(x + 1, y - 1)
    val topLeft: CellIndex get() = CellIndex(x - 1, y - 1)

    operator fun plus(other: CellIndex) = CellIndex(x + other.x, y + other.y)
    operator fun minus(other: CellIndex) = CellIndex(x - other.x, y - other.y)

    fun modularIndex(from: List<List<Any>>): CellIndex {
        var x = this.x
        var y = this.y

        if (x > 0) x %= from.size
        while (x < 0) x += from.size

        if (y > 0) y %= from[0].size
        while (y < 0) x += from[0].size

        return CellIndex(x, y)
    }

    val neighbors: Set<CellIndex> get() = setOf(top, right, bottom, left)
    val diagonalNeighbors: Set<CellIndex> get() = setOf(topRight, bottomRight, bottomLeft, topLeft)

    fun <T> isOutsideBoundsOf(grid: List<List<T>>): Boolean =
        x < 0 || y < 0 || x >= grid.size || y >= grid[0].size

    fun <T> isOutsideBoundsOf(grid: Array<Array<T>>): Boolean =
        x < 0 || y < 0 || x >= grid.size || y >= grid[0].size

    fun <T> isInsideBoundsOf(grid: List<List<T>>): Boolean = !isOutsideBoundsOf(grid)

    fun <T> isInsideBoundsOf(grid: Array<Array<T>>): Boolean = !isOutsideBoundsOf(grid)

    fun next(d: Dir): CellIndex = when (d) {
        Dir.U -> top
        Dir.D -> bottom
        Dir.L -> left
        Dir.R -> right
        Dir.UR -> topRight
        Dir.DR -> bottomRight
        Dir.DL -> bottomLeft
        Dir.UL -> topLeft
    }

    fun manhattanDistance(to: CellIndex) = abs(x - to.x) + abs(y - to.y)

    fun directionFrom(other: CellIndex): Dir {
        return when (other) {
            this.right -> Dir.L
            this.bottom -> Dir.U
            this.top -> Dir.D
            this.left -> Dir.R
            this.topRight -> Dir.DL
            this.bottomRight -> Dir.UL
            this.bottomLeft -> Dir.UR
            else -> Dir.DR
        }
    }

    fun printable(): String = "[$x,$y]"
}

operator fun <T> Array<Array<T>>.get(index: CellIndex): T { return this[index.x][index.y] }
operator fun <T> Array<Array<T>>.set(index: CellIndex, value: T) { this[index.x][index.y] = value }

operator fun Array<CharArray>.get(index: CellIndex): Char { return this[index.x][index.y] }
operator fun Array<CharArray>.set(index: CellIndex, value: Char) { this[index.x][index.y] = value }

operator fun List<CharArray>.get(index: CellIndex): Char { return this[index.x][index.y] }
operator fun List<CharArray>.set(index: CellIndex, value: Char) { this[index.x][index.y] = value }

operator fun <T> List<List<T>>.get(index: CellIndex): T { return this[index.x][index.y] }
operator fun <T> List<MutableList<T>>.set(index: CellIndex, value: T) { this[index.x][index.y] = value }

fun List<CellIndex>.printable(): String =
    fold("") { acc, e -> "$acc${e.printable()}, "}.dropLast(2)

fun List<CellIndex>.areInStraightLine(): Boolean =
    all { it.x == this[0].x } || all { it.y == this[0].y }

fun List<CellIndex>.areaOfPolygon(): Long {
    var area = 0L
    var j = size - 1
    for (i in indices) {
        area += (this[j].x + this[i].x).toLong() * (this[j].y - this[i].y).toLong()
        j = i
    }
    return abs(area / 2)
}
