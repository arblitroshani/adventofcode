package year2024.day21

import framework.solution
import util.common.CellIndex
import util.common.algorithms.dijkstraExhaustive
import util.common.get

fun main() = solution<List<String>>(2024, 21) {

    fun cacheSteps(inputKeypad: List<List<Char>>): Map<Pair<Char, Char>, List<String>> {
        val allButtons = inputKeypad.flatMapIndexed { r, row -> List(row.size) { CellIndex(r, it) } }
        val stepsForInputKeypad = mutableMapOf<Pair<Char, Char>, List<String>>()

        for (i in allButtons) {
            for (j in allButtons) {
                val startButton = inputKeypad[i]
                val endButton = inputKeypad[j]

                if (i == j || startButton == '-' || endButton == '-') {
                    stepsForInputKeypad[startButton to endButton] = listOf("A")
                    continue
                }

                val allOptimalSteps: List<List<CellIndex>> = dijkstraExhaustive(
                    start = i,
                    adjacency = { node ->
                        node.neighbors
                            .filter { it.isInsideBoundsOf(inputKeypad) && inputKeypad[it] != '-' }
                            .map { it to 1 }
                    },
                    isGoal = { it == j }
                ).map { step -> step.map { it.first } }

                stepsForInputKeypad[startButton to endButton] = allOptimalSteps.map { step ->
                    step.windowed(2) { (l, r) ->
                        if (l.r == r.r) if (l.c < r.c) '>' else '<'
                        else if (l.r < r.r) 'v' else '^'
                    }.joinToString("")
                }
            }
        }
        return stepsForInputKeypad
    }

    val stepsForNumpad = cacheSteps(
        inputKeypad = listOf(
            listOf('7', '8', '9'),
            listOf('4', '5', '6'),
            listOf('1', '2', '3'),
            listOf('-', '0', 'A'),
        ),
    )

    val stepsForDirectionalKeypad = cacheSteps(
        inputKeypad = listOf(
            listOf('-', '^', 'A'),
            listOf('<', 'v', '>'),
        ),
    )

    parseInput { it }

    partOne {
        fun inputKeypadSequencesFor(s: String, useNumpad: Boolean = false): List<String> {
            val inputKeypadSteps = if (useNumpad) stepsForNumpad else stepsForDirectionalKeypad
            val strings = mutableListOf<String>()
            "A$s".toList().windowed(2) { (l, r) ->
                val currentStrings = if (strings.isEmpty()) listOf("") else strings.toList()
                strings.clear()
                for (step in inputKeypadSteps[l to r]!!) {
                    val joiner = if (!useNumpad && l == r) "" else "A"
                    currentStrings.forEach { str -> strings.add("$str$joiner$step") }
                }
            }
            return strings.map { str -> str.drop(1).plus('A') }
        }

        fun shortestSequenceLength(s: String): Int =
            inputKeypadSequencesFor(s, true).minOf { sequence ->
                inputKeypadSequencesFor(sequence).minOf { nextSequence ->
                    inputKeypadSequencesFor(nextSequence).minOf(String::length)
                }
            }

        fun complexity(s: String): Int =
            s.dropLast(1).toInt() * shortestSequenceLength(s)

        it.sumOf(::complexity)
    }

    partOneTest {
        """
            029A
            980A
            179A
            456A
            379A
        """.trimIndent() shouldOutput 126384
    }
}
