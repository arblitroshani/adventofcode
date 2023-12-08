package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.util.InputReader
import com.arblitroshani.adventofcode.util.print

fun main() {
    val input = InputReader(2023, 2).read()

    var sumPt1 = 0
    var sumPt2 = 0

    input.forEachIndexed { index, line ->
        val game = line.split(":").last().trim()
        sumPt2 += powerOfGame(game)
        if (isGamePossible(game)) sumPt1 += (index + 1)
    }

    sumPt1.print()
    sumPt2.print()
}

fun isGamePossible(game: String): Boolean {
    for (gamePart in game.split(";").map(String::trim))
        for (pick in gamePart.split(",").map(String::trim)) {
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
        .map(String::trim)
        .forEach { gamePart ->
            gamePart
                .split(",")
                .map(String::trim)
                .forEach { pick ->
                    val value = pick.split(" ").first().toInt()
                    if (pick.endsWith("red")) r = maxOf(r, value)
                    if (pick.endsWith("green")) g = maxOf(g, value)
                    if (pick.endsWith("blue")) b = maxOf(b, value)
                }
        }
    return r * g * b
}
