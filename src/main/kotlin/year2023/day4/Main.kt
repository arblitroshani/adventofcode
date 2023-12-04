package year2023.day4

import java.io.File

fun main() {
    val input = File("./src/main/kotlin/year2023/day4/input.txt").readLines()
    val copies = Array(input.size) { 1 }
    val sumPt1 = input.mapIndexed { index, line ->
        line.split(":").last().trim()
            .split("|").map { it.split(" ").filterNot(String::isBlank).toSet() }
            .reduce(Set<String>::intersect).size
            .let { ticketsWon ->
                for (i in 1 .. ticketsWon) copies[index + i] += copies[index]
                1 shl ticketsWon - 1
            }
    }.sum()
    println("pt1: $sumPt1, pt2: ${copies.sum()}")
}
