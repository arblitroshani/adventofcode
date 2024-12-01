package year2023.day02

import framework.solution

private typealias Input = List<String>

fun main() = solution<Input>(2023, 2) {

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

    parseInput { lines ->
        lines.map { it.split(':').last().trim() }
    }

    partOne { input ->
        input
            .mapIndexed { index, game -> game to index}
            .filter { isGamePossible(it.first) }
            .sumOf { it.second + 1 }
    }

    partTwo { input ->
        input.sumOf(::powerOfGame)
    }

    val testInput = """
        Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
        Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
        Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
        Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 8
    }

    partTwoTest {
        testInput shouldOutput 2286
    }
}
