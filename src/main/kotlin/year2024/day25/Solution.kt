package year2024.day25

import framework.solution
import util.InputParsing

private data class Input(val locks: List<List<Int>>, val keys: List<List<Int>>)

fun main() = solution<Input>(2024, 25) {

    parseInput { lines ->
        val locks = mutableListOf<List<Int>>()
        val keys = mutableListOf<List<Int>>()
        InputParsing.splitListByEmptyLines(lines).forEach {
            val info = mutableListOf<Int>()
            if (it[0][0] == '#') {
                for (i in 0 .. 4)
                    for (j in 0 .. 6) {
                        if (it[j][i] == '#') continue
                        info.add(j)
                        break
                    }
                locks.add(info)
            } else {
                for (i in 0 .. 4)
                    for (j in 6 downTo 0) {
                        if (it[j][i] == '#') continue
                        info.add(6 - j)
                        break
                    }
                keys.add(info)
            }
        }
        Input(locks, keys)
    }

    partOne { (locks, keys) ->
        locks.sumOf { lock ->
            val expectedKeyMaxHeights = lock.map { 7 - it }
            keys.count { key ->
                key.zip(expectedKeyMaxHeights)
                    .all { it.first <= it.second }
            }
        }
    }
}
