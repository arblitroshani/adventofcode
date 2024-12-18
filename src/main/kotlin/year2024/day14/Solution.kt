package year2024.day14

import framework.solution
import util.common.CellIndex

private data class Robot(val pos: CellIndex, val v: CellIndex)

fun main() = solution<List<Robot>>(2024, 14) {

    fun Robot.futurePositionAfter(t: Int, w: Int, h: Int): CellIndex {
        val futurePosRow = pos.r + t * (v.r + h)
        val futurePosCol = pos.c + t * (v.c + w)
        return CellIndex(futurePosRow % h, futurePosCol % w)
    }

    fun safetyFactorAfterJumpingFor100Seconds(robots: List<Robot>, w: Int, h: Int): Int {
        val quadrantCounters = IntArray(4)
        robots
            .map { it.futurePositionAfter(100, w, h) }
            .forEach { fp ->
                if (fp.r < h / 2 && fp.c < w / 2) quadrantCounters[0]++
                if (fp.r < h / 2 && fp.c > w / 2) quadrantCounters[1]++
                if (fp.r > h / 2 && fp.c < w / 2) quadrantCounters[2]++
                if (fp.r > h / 2 && fp.c > w / 2) quadrantCounters[3]++
            }
        return quadrantCounters.fold(1) { acc, i -> acc * i }
    }

    parseInput { lines ->
        lines.map { line ->
            val (c, r, vC, vR) = line
                .split("p=", ",", " v=", ",")
                .filterNot(String::isEmpty)
                .map(String::toInt)
            Robot(pos = CellIndex(r, c), v = CellIndex(vR, vC))
        }
    }

    partOne { robots ->
        safetyFactorAfterJumpingFor100Seconds(robots, w = 101, h = 103)
    }

    partTwo { robots ->
        val w = 101
        val h = 103
        val upperBound = 10000

        fun robotPositionsAfter(t: Int) = robots
            .map { it.futurePositionAfter(t, w, h) }
            .toSet()

        fun printRobotsAfter(t: Int) {
            val rps = robotPositionsAfter(t)
            (0 until h).forEach { r ->
                (0 until h).forEach { c ->
                    print(if (CellIndex(r, c) in rps) "#" else ".")
                }
                println()
            }
        }

        val timesToMaxRobotsInLine = (0 until upperBound).map { t ->
            robotPositionsAfter(t)
                .groupBy(CellIndex::r)
                .map { it.value.size }
                .max() to t
        }

        timesToMaxRobotsInLine
            .maxBy { it.first }
            .second
            .also(::printRobotsAfter)
    }

    partOneTest {
        """
            p=0,4 v=3,-3
            p=6,3 v=-1,-3
            p=10,3 v=-1,2
            p=2,0 v=2,-1
            p=0,0 v=1,3
            p=3,0 v=-2,-2
            p=7,6 v=-1,-3
            p=3,0 v=-1,-2
            p=9,3 v=2,3
            p=7,3 v=-1,2
            p=2,4 v=2,-3
            p=9,5 v=-3,-3
        """.trimIndent() with {
            safetyFactorAfterJumpingFor100Seconds(it, w = 11, h = 7)
        } shouldOutput 12
    }
}
