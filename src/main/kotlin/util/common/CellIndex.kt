package util.common

import kotlin.math.abs

data class CellIndex(val r: Int, val c: Int) {
    val top: CellIndex get() = CellIndex(r - 1, c)
    val right: CellIndex get() = CellIndex(r, c + 1)
    val bottom: CellIndex get() = CellIndex(r + 1, c)
    val left: CellIndex get() = CellIndex(r, c - 1)

    private val topRight: CellIndex get() = CellIndex(r - 1, c + 1)
    private val bottomRight: CellIndex get() = CellIndex(r + 1, c + 1)
    private val bottomLeft: CellIndex get() = CellIndex(r + 1, c - 1)
    private val topLeft: CellIndex get() = CellIndex(r - 1, c - 1)

    operator fun plus(other: CellIndex) = CellIndex(r + other.r, c + other.c)
    operator fun minus(other: CellIndex) = CellIndex(r - other.r, c - other.c)

    fun modularIndex(from: List<List<Any>>): CellIndex {
        var x = this.r
        var y = this.c

        if (x > 0) x %= from.size
        while (x < 0) x += from.size

        if (y > 0) y %= from[0].size
        while (y < 0) x += from[0].size

        return CellIndex(x, y)
    }

    val neighbors: Set<CellIndex> get() = setOf(top, right, bottom, left)
    val diagonalNeighbors: Set<CellIndex> get() = setOf(topRight, bottomRight, bottomLeft, topLeft)
    val allNeighbors: Set<CellIndex> get() = neighbors + diagonalNeighbors

    fun <T> isOutsideBoundsOf(grid: List<List<T>>): Boolean =
        r < 0 || c < 0 || r >= grid.size || c >= grid[0].size

    fun <T> isOutsideBoundsOf(grid: Array<Array<T>>): Boolean =
        r < 0 || c < 0 || r >= grid.size || c >= grid[0].size

    fun isOutsideBoundsOf(gridSize: Int): Boolean =
        r < 0 || c < 0 || r >= gridSize || c >= gridSize

    fun <T> isInsideBoundsOf(grid: List<List<T>>): Boolean = !isOutsideBoundsOf(grid)

    fun <T> isInsideBoundsOf(grid: Array<Array<T>>): Boolean = !isOutsideBoundsOf(grid)

    fun isInsideBoundsOf(gridSize: Int): Boolean = !isOutsideBoundsOf(gridSize)

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

    fun manhattanDistance(to: CellIndex) = abs(r - to.r) + abs(c - to.c)

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

    fun printable(): String = "[$r,$c]"
}

operator fun <T> Array<Array<T>>.get(index: CellIndex): T { return this[index.r][index.c] }
operator fun <T> Array<Array<T>>.set(index: CellIndex, value: T) { this[index.r][index.c] = value }

operator fun Array<CharArray>.get(index: CellIndex): Char { return this[index.r][index.c] }
operator fun Array<CharArray>.set(index: CellIndex, value: Char) { this[index.r][index.c] = value }

operator fun List<CharArray>.get(index: CellIndex): Char { return this[index.r][index.c] }
operator fun List<CharArray>.set(index: CellIndex, value: Char) { this[index.r][index.c] = value }

operator fun <T> List<List<T>>.get(index: CellIndex): T { return this[index.r][index.c] }
operator fun <T> List<MutableList<T>>.set(index: CellIndex, value: T) { this[index.r][index.c] = value }

fun List<CellIndex>.printable(): String =
    fold("") { acc, e -> "$acc${e.printable()}, "}.dropLast(2)

fun List<CellIndex>.areInStraightLine(): Boolean =
    all { it.r == this[0].r } || all { it.c == this[0].c }

fun List<CellIndex>.areaOfPolygon(): Long {
    var area = 0L
    var j = size - 1
    for (i in indices) {
        area += (this[j].r + this[i].r).toLong() * (this[j].c - this[i].c).toLong()
        j = i
    }
    return abs(area / 2)
}
