package year2023.day2

import java.io.File

fun main() {
    val input = File("./src/main/kotlin/year2023/day2/input.txt").readLines()

    var sum = 0
    var sumPt2 = 0

    input.forEachIndexed { index, line ->
        val game = line.split(":").last().trim()
        sumPt2 += powerOfGame(game)
        if (isGamePossible(game)) sum += (index + 1)
    }

    println("pt1: $sum")
    println("pt2: $sumPt2")
}

fun isGamePossible(game: String): Boolean {
    for (gamePart in game.split(";").map { it.trim() })
        for (pick in gamePart.split(",").map { it.trim() }) {
            val value = pick.split(" ").first().toInt()
            if (pick.endsWith("red") && value > 12) return false
            if (pick.endsWith("green") && value > 13) return false
            if (pick.endsWith("blue") && value > 14) return false
        }
    return true
}

fun powerOfGame(game: String): Int {
    var r = 0
    var g = 0
    var b = 0

    game
        .split(";")
        .map { it.trim() }
        .forEach { gamePart ->
            gamePart
                .split(",")
                .map { it.trim() }
                .forEach { pick ->
                    val value = pick.split(" ").first().toInt()
                    if (pick.endsWith("red")) r = r.coerceAtLeast(value)
                    if (pick.endsWith("green")) g = g.coerceAtLeast(value)
                    if (pick.endsWith("blue")) b = b.coerceAtLeast(value)
                }
        }
    return r * g * b
}
