package year2024.day15

import framework.solution
import util.InputParsing
import util.common.CellIndex
import util.common.Dir
import util.common.get
import util.common.set

private data class Input(val grid: List<CharArray>, val moveDirections: List<Dir>)

fun main() = solution<Input>(2024, 15) {

    fun gpsCoordinates(grid: List<CharArray>, target: Char): Int =
        grid.mapIndexed { r, row ->
            row.mapIndexed { c, ch ->
                if (ch == target) (100 * r) + c else 0
            }.sum()
        }.sum()

    fun robotLocation(grid: List<CharArray>): CellIndex =
        grid
            .mapIndexedNotNull { r, row ->
                val indexOfRobot = row.indexOf('@')
                if (indexOfRobot != -1) CellIndex(r, indexOfRobot) else null
            }.firstOrNull() ?: error("There should be at least 1 robot")

    fun shiftInDirection(grid: List<CharArray>, robotLocation: CellIndex, dir: Dir): CellIndex {
        fun firstEmptySpaceInDir(): CellIndex? {
            var nextSpace = robotLocation.next(dir)
            while (true) {
                if (grid[nextSpace] == '.') return nextSpace
                if (grid[nextSpace] == '#') return null
                nextSpace = nextSpace.next(dir)
            }
        }
        var target = firstEmptySpaceInDir() ?: return robotLocation
        while (true) {
            if (target == robotLocation) break
            val toMove = target.next(dir.cw.cw)
            grid[target] = grid[toMove]
            target = toMove
        }
        return robotLocation.next(dir)
    }

    parseInput { lines ->
        val (gridLines, movesLines) = InputParsing.splitListByEmptyLines(lines)
        Input(
            grid = gridLines.map(String::toCharArray),
            moveDirections = movesLines.joinToString(separator = "").mapNotNull {
                when (it) {
                    '<' -> Dir.L
                    '>' -> Dir.R
                    '^' -> Dir.U
                    'v' -> Dir.D
                    else -> error("Invalid direction")
                }
            },
        )
    }

    partOne { (grid, moveDirections) ->
        var robotLocation = robotLocation(grid)
        grid[robotLocation] = '.'
        moveDirections.forEach { dir ->
            robotLocation = shiftInDirection(grid, robotLocation, dir)
        }
        gpsCoordinates(grid, target = 'O')
    }

    partTwo { (grid, moveDirections) ->
        val expandedGrid = grid.map { row ->
            row.joinToString(separator = "") { ch ->
                when (ch) {
                    '#', '.' -> "$ch$ch"
                    'O' -> "[]"
                    '@' -> "@."
                    else -> error("Invalid character")
                }
            }.toCharArray()
        }

        var robotLocation = robotLocation(expandedGrid)
        expandedGrid[robotLocation] = '.'

        fun shiftInDirection(dir: Dir) {
            if (!dir.isVertical) {
                robotLocation = shiftInDirection(expandedGrid, robotLocation, dir)
                return
            }

            fun canShift(index: CellIndex, dir: Dir): Boolean {
                val next = index.next(dir)
                return when (expandedGrid[next]) {
                    '.' -> true
                    '#' -> false
                    ']' -> canShift(next.left, dir) && canShift(next, dir)
                    '[' -> canShift(next, dir) && canShift(next.right, dir)
                    else -> error("Invalid grid character")
                }
            }

            fun indexesToShift(index: CellIndex, dir: Dir): Set<CellIndex> {
                val indexesToShift = mutableSetOf<CellIndex>()
                val next = index.next(dir)
                if (expandedGrid[next] == '[') {
                    indexesToShift += indexesToShift(next, dir)
                    indexesToShift += indexesToShift(next.right, dir)
                }
                if (expandedGrid[next] == ']') {
                    indexesToShift += indexesToShift(next, dir)
                    indexesToShift += indexesToShift(next.left, dir)
                }
                return indexesToShift + index
            }

            if (!canShift(robotLocation, dir)) return
            val indexesToShift = indexesToShift(robotLocation, dir)
            val indexGroups = indexesToShift.groupBy { it.r }.toList()
            val sortedIndexGroups =
                if (dir == Dir.U) indexGroups.sortedBy { it.first }
                else indexGroups.sortedByDescending { it.first }
            sortedIndexGroups
                .flatMap { (_, group) -> group }
                .forEach { index ->
                    expandedGrid[index.next(dir)] = expandedGrid[index]
                    expandedGrid[index] = '.'
                }
            robotLocation = robotLocation.next(dir)
        }

        moveDirections.forEach(::shiftInDirection)
        gpsCoordinates(expandedGrid, target = '[')
    }

    val testInput = """
        ##########
        #..O..O.O#
        #......O.#
        #.OO..O.O#
        #..O@..O.#
        #O#..O...#
        #O..O..O.#
        #.OO.O.OO#
        #....O...#
        ##########

        <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
        vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
        ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
        <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
        ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
        ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
        >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
        <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
        ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
        v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 10092
    }

    partTwoTest {
        testInput shouldOutput 9021
    }
}
