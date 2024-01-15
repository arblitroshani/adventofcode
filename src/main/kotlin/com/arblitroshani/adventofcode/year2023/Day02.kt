package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.AocPuzzle

typealias Input23d02 = List<String>

class Day02: AocPuzzle<Input23d02>() {

    override fun parseInput(lines: List<String>) =
        lines.map { it.split(':').last().trim() }

    override fun partOne(): Int =
        input
            .mapIndexed { index, game -> game to index}
            .filter { isGamePossible(it.first) }
            .sumOf { it.second + 1 }

    override fun partTwo(): Int =
        input.sumOf(::powerOfGame)

    private fun isGamePossible(game: String): Boolean {
        for (gamePart in game.split(";").map(String::trim))
            for (pick in gamePart.split(",").map(String::trim)) {
                val value = pick.split(" ").first().toInt()
                if (pick.endsWith("red") && value > 12) return false
                if (pick.endsWith("green") && value > 13) return false
                if (pick.endsWith("blue") && value > 14) return false
            }
        return true
    }

    private fun powerOfGame(game: String): Int {
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
}

fun main() = Day02().solve(
    expectedAnswerForSampleInP1 = 8,
    expectedAnswerForSampleInP2 = 2286,
)
