package year2025.day02

import framework.solution

private typealias Input = List<LongRange>

fun main() = solution<Input>(2025, 2) {

    parseInput { lines ->
        lines
            .joinToString()
            .split(',')
            .map {
                val (start, end) = it
                    .split('-')
                    .map(String::toLong)
                start until end + 1
            }
    }

    partOne { input ->
        var sum = 0L
        for (range in input) {
            for (i in range) {
                val s = i.toString()
                if (s.length % 2 == 1) continue
                val firstHalf = s.take(s.length / 2)
                val secondHalf = s.takeLast(s.length / 2)
                if (firstHalf == secondHalf) sum += i
            }
        }
        sum
    }

    partTwo { input ->
        var sum = 0L
        for (range in input) {
            for (i in range) {
                val s = i.toString()
                for (j in 2 until s.length + 1) {
                    if (s.length % j != 0) continue
                    val parts = s.chunked(s.length / j)
                    if (parts.all { it == parts[0] }) {
                        sum += i
                        break
                    }
                }
            }
        }
        sum
    }

    val testInput = """
        11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 1227775554
    }

    partTwoTest {
        testInput shouldOutput 4174379265
    }
}
