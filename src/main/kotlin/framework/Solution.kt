package framework

import util.printGreen
import util.printRed
import util.printYellow
import java.io.File
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.measureTimedValue

typealias InputParser<T> = (List<String>) -> T
typealias Part<T> = (T) -> Any

fun <T: Any> solution(year: Int, day: Int, block: Solution<T>.() -> Unit) =
    Solution<T>(Day(year, day))
        .apply(block)
        .calculateAnswers()

class Solution<T: Any>(private val day: Day) {

    private lateinit var inputParser: InputParser<T>
    private lateinit var partOneSolver: Part<T>
    private lateinit var partTwoSolver: Part<T>

    private val partOneTest by lazy { PartTestRunner(inputParser) }
    private val partTwoTest by lazy { PartTestRunner(inputParser) }

    fun parseInput(inputParser: InputParser<T>) {
        this.inputParser = inputParser
    }

    fun partOne(part: Part<T>) {
        partOneSolver = part
    }

    fun partTwo(part: Part<T>) {
        partTwoSolver = part
    }

    fun partOneTest(block: PartTestRunner<T>.() -> Unit) =
        partOneTest.apply(block)

    fun partTwoTest(block: PartTestRunner<T>.() -> Unit) =
        partTwoTest.apply(block)

    fun calculateAnswers() {
        val rawInputLines: List<String> = InputReader(day).read()
        val input: T = inputParser(rawInputLines)

        println("────────────── Part One ──────────────")
        executePart(1, input, partOneSolver, partOneTest)

        if (::partTwoSolver.isInitialized) {
            println("────────────── Part Two ──────────────")
            executePart(2, input, partTwoSolver, partTwoTest)
        }
    }

    private fun executePart(partNumber: Int, input: T, part: Part<T>?, testRunner: PartTestRunner<T>) {
        if (part == null) return

        testRunner.test(part)

        val (result, duration) = try {
            measureTimedValue { part(input) }
        } catch (error: Throwable) {
            printRed {
                println("Exception while processing part $partNumber")
            }
            error.printStackTrace()
            return
        }

        val verifiedSolution = day.solutionFile(partNumber)
            .takeIf(File::isFile)
            ?.readText()
            ?.trim()

        if (verifiedSolution != null) {
            if (result.toString() == verifiedSolution) {
                printGreen {
                    println("Verified solution: $result")
                }
            } else {
                printRed {
                    println("Wrong solution")
                    println(" ⇒ Got: $result")
                    println(" ⇒ Expected: $verifiedSolution")
                }
            }
        } else {
            printYellow {
                println("Unverified solution: $result")
            }
        }

        printYellow {
            println("Measured time: ${duration.rounded()}")
        }
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