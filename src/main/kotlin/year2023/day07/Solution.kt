package year2023.day07

import framework.solution

private typealias Input = List<String>

fun main() = solution<Input>(2023, 7) {

    fun solve(input: List<String>, isPart2: Boolean = false): Int =
        input
            .map { line ->
                val (hand, bid) = line.split(' ')
                Hand.create(hand, bid.toInt(), isPart2)
            }
            .sorted()
            .foldIndexed(0) { i, acc, hand -> acc + (i + 1) * hand.bid }

    parseInput { lines ->
        lines
    }

    partOne { input ->
        solve(input, isPart2 = false)
    }

    partTwo { input ->
        solve(input, isPart2 = true)
    }

    val testInput = """
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 6440
    }

    partTwoTest {
        testInput shouldOutput 5905
    }
}
