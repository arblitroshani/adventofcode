package year2024.day13

import framework.solution
import util.InputParsing
import kotlin.math.roundToLong

data class ClawMachine(
    val ax: Int, val ay: Int,
    val bx: Int, val by: Int,
    val p1: Long, val p2: Long,
)

fun main() = solution<List<ClawMachine>>(2024, 13) {

    fun tokens(cm: ClawMachine) : Pair<Long, Long> {
        val v1 = 1.0 * (cm.p1 - cm.p2) / (cm.ax - cm.ay)
        val v2 = 1.0 * (cm.bx - cm.by) / (cm.ax - cm.ay)
        val b = ((cm.p1 - cm.ax * v1) / (cm.bx - cm.ax * v2)).roundToLong()
        val a = (v1 - v2 * b).roundToLong()

        if ((cm.ax * a + cm.bx * b) != cm.p1) return 0L to 0L
        if ((cm.ay * a + cm.by * b) != cm.p2) return 0L to 0L
        return a to b
    }

    parseInput { lines ->
        InputParsing.splitListByEmptyLines(lines).map {
            val (ax, ay) = it[0].split(": X+", ", Y+").drop(1).map(String::toInt)
            val (bx, by) = it[1].split(": X+", ", Y+").drop(1).map(String::toInt)
            val (p1, p2) = it[2].split(": X=", ", Y=").drop(1).map(String::toLong)
            ClawMachine(ax, ay, bx, by, p1, p2)
        }
    }

    partOne { clawMachines ->
        clawMachines
            .map(::tokens)
            .filterNot { (a, b) -> a > 100 || b > 100 }
            .sumOf { (a, b) -> 3 * a + b }
    }

    partTwo { clawMachines ->
        val offset = 10000000000000L
        clawMachines
            .map { it.copy(p1 = it.p1 + offset, p2 = it.p2 + offset) }
            .map(::tokens)
            .sumOf { (a, b) -> 3 * a + b }
    }

    partOneTest {
        """
            Button A: X+94, Y+34
            Button B: X+22, Y+67
            Prize: X=8400, Y=5400

            Button A: X+26, Y+66
            Button B: X+67, Y+21
            Prize: X=12748, Y=12176

            Button A: X+17, Y+86
            Button B: X+84, Y+37
            Prize: X=7870, Y=6450

            Button A: X+69, Y+23
            Button B: X+27, Y+71
            Prize: X=18641, Y=10279
        """.trimIndent() shouldOutput 480
    }
}
