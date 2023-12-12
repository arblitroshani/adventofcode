package com.arblitroshani.adventofcode

import com.arblitroshani.adventofcode.util.InputReader
import com.arblitroshani.adventofcode.util.print
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.TimedValue
import kotlin.time.measureTimedValue

/**
 * Abstract class representing an Advent of Code puzzle.
 *
 * @param T The type of the input that will be parsed and used in the puzzle.
 *
 * The `year` and `day` properties are extracted from the structure of the input data:
 *  - `year` represents the advent-of-code year and is derived from the name of the enclosing folder.
 *  - `day` represents the day of the puzzle and is derived from the file name.
 */
abstract class AocPuzzle<T> {

    private val year: Int
    private val day: Int
    private val inputReader: InputReader

    init {
        val (folderName, fileName) = javaClass.kotlin.qualifiedName!!.split('.').takeLast(2)
        year = folderName.takeLast(4).toInt()
        day = fileName.drop(3).toInt()
        inputReader = InputReader(year, day)
    }

    fun solve(
        expectedAnswerForSampleInP1: Any? = null,
        expectedAnswerForSampleInP2: Any? = null,
    ) {
        checkSample(expectedAnswerForSampleInP1, expectedAnswerForSampleInP2)
        println("\n=== DAY $day, $year ===")
        calculateAnswers()
        "=".repeat(if (day > 9) 20 else 19).print()
    }

    abstract fun parseInput(input: List<String>): T

    abstract fun partOne(input: T): Any

    abstract fun partTwo(input: T): Any

    private fun checkSample(
        expectedAnswerForSampleInP1: Any? = null,
        expectedAnswerForSampleInP2: Any? = null,
    ) {
        val sampleInput = inputReader.readSample()
        val sampleInputParsed = parseInput(sampleInput)
        if (expectedAnswerForSampleInP1 != null) {
            val myAnswer = partOne(sampleInputParsed)
            check(myAnswer == expectedAnswerForSampleInP1) {
                "Sample solution for P1 is wrong! Got $myAnswer, expected $expectedAnswerForSampleInP1."
            }
        }
        if (expectedAnswerForSampleInP2 != null) {
            val myAnswer = partTwo(sampleInputParsed)
            check(myAnswer == expectedAnswerForSampleInP2) {
                "Sample solution for P2 is wrong! Got $myAnswer, expected $expectedAnswerForSampleInP2!"
            }
        }
    }

    private fun calculateAnswers() {
        val input = inputReader.read()
        val (parsedInput, parsingDuration) = measureTimedValue { parseInput(input) }
        val partOne = measureTimedValue { partOne(parsedInput) }
        val partTwo = measureTimedValue { partTwo(parsedInput) }
        printAnswers(parsingDuration, partOne, partTwo)
    }

    private fun printAnswers(
        parsingDuration: Duration,
        partOne: TimedValue<Any>,
        partTwo: TimedValue<Any>,
    ) {
        val padding = maxOf(
            partOne.value.toString().length,
            partTwo.value.toString().length,
        ) + 20
        println("Parsing:".padEnd(padding, ' ') + parsingDuration.rounded())
        println("-Part 1: ${partOne.value}".padEnd(padding, ' ') + partOne.duration.rounded())
        println("-Part 2: ${partTwo.value}".padEnd(padding, ' ') + partTwo.duration.rounded())
    }

    private fun Duration.rounded(): String {
        val regex = """(\d+(\.\d+)?)([a-z]+)""".toRegex()
        val matchResult = regex.find(toString())
        return if (matchResult == null) {
            toString()
        } else {
            val (number, _, unit) = matchResult.destructured
            val roundedNumber = number.toDoubleOrNull()
                ?.let { "%.2f".format(Locale.US, it) }
                ?: number
            "$roundedNumber $unit"
        }
    }
}
