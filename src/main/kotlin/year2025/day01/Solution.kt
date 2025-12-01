package year2025.day01

import framework.solution
import kotlin.math.abs

fun main() = solution<List<Int>>(2025, 1) {

    parseInput { lines ->
        lines.map { line ->
            val dir = line.first()
            val amount = line.drop(1).toInt()
            if (dir == 'L') -amount else amount
        }
    }

    partOne { input ->
        var position = 50
        var cnt = 0

        input.forEach { amount ->
            if (position == 0) cnt++
            position = (position + amount) % 100
        }
        cnt
    }

    partTwo { input ->
        var position = 50
        var cnt = 0

        input.forEach { amount ->
            repeat(abs(amount)) {
                if (position == 0) cnt++
                position = (position + (if (amount > 0) 1 else -1)) % 100
            }
        }
        cnt
    }

    val testInput = """
        L68
        L30
        R48
        L5
        R60
        L55
        L1
        L99
        R14
        L82
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 3
    }

    partTwoTest {
        testInput shouldOutput 6
    }
}
