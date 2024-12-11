package year2024.day11

import framework.solution

fun main() = solution<List<Long>>(2024, 11) {

    val memo = mutableMapOf<Pair<Long, Int>, Long>()

    fun numberOfStones(num: Long, blinks: Int): Long {
        if (blinks == 0) return 1L
        return memo.getOrPut(num to blinks) {
            val s = num.toString()
            when {
                num == 0L -> numberOfStones(1L, blinks - 1)
                s.length % 2 == 1 -> numberOfStones(num * 2024, blinks - 1)
                else -> {
                    val halfLen = s.length / 2
                    val stonesLeft = numberOfStones(s.dropLast(halfLen).toLong(), blinks - 1)
                    val stonesRight = numberOfStones(s.drop(halfLen).toLong(), blinks - 1)
                    stonesLeft + stonesRight
                }
            }
        }
    }

    parseInput { lines ->
        lines.first().split(' ').map(String::toLong)
    }

    partOne { input ->
        input.sumOf { numberOfStones(it, 25) }
    }

    partTwo { input ->
        input.sumOf { numberOfStones(it, 75) }
    }

    partOneTest {
        "125 17" shouldOutput 55312
    }
}
