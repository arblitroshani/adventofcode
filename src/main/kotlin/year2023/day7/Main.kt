package year2023.day7

import util.InputReader

fun main() {
    val ip = InputReader(2023, 7).read()

    println("Solution pt1: ${solve(ip)}")
    println("Solution pt2: ${solve(ip, true)}")
}

fun solve(input: List<String>, isPart2: Boolean = false): Int =
    input
        .map { line ->
            val (hand, bid) = line.split(" ")
            CamelHand(Hand.create(hand, isPart2), bid.toInt())
        }
        .sorted()
        .foldIndexed(0) { i, acc, hand -> acc + (i + 1) * hand.bid }
