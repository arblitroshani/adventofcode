package year2024.day22

import framework.solution

fun main() = solution<List<Long>>(2024, 22) {

    fun next(sn: Long): Long {
        var next = sn
        next = ((next shl 6) xor next) and 0xFFFFFF
        next = ((next shr 5) xor next) and 0xFFFFFF
        next = ((next shl 11) xor next) and 0xFFFFFF
        return next
    }

    parseInput { it.map(String::toLong) }

    partOne { secretNumbers ->
        secretNumbers.sumOf {
            var n = it
            repeat(2000) { n = next(n) }
            n
        }
    }

    partTwo { secretNumbers ->
        val allDiffQuadruples = secretNumbers.map { sn ->
            var nextSn = sn
            val lastDigitOfSecretNumbers = List(size = 2000) {
                nextSn = next(nextSn)
                return@List (nextSn % 10).toInt()
            }

            val listMinusFirst = lastDigitOfSecretNumbers.drop(1)
            val diffs = listMinusFirst
                .mapIndexed { i, el -> el - lastDigitOfSecretNumbers[i] }
                .zip(listMinusFirst)

            val diffQuadruples = mutableMapOf<List<Int>, Int>()
            diffs.windowed(4) { pairs ->
                val key = pairs.map { it.first }
                val value = pairs.last().second
                diffQuadruples.putIfAbsent(key, value)
            }
            diffQuadruples
        }

        allDiffQuadruples
            .take(3)
            .maxOf { diffQuadruple ->
                diffQuadruple.maxOf { (k, _) ->
                    allDiffQuadruples.sumOf { it[k] ?: 0 }
                }
            }
    }

    partOneTest {
        """
            1
            10
            100
            2024
        """.trimIndent() shouldOutput 37327623L
    }

    partTwoTest {
        """
            1
            2
            3
            2024
        """.trimIndent() shouldOutput 23
    }
}
