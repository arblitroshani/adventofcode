package framework

import util.printGreen
import util.printRed

sealed class TestResult
data object TestPassed : TestResult()
data class TestWrongOutput(val output: Any?) : TestResult()
data class TestException(val cause: Throwable) : TestResult()

data class Test<T: Any>(val input: String, val expectedValue: Any?, val part: Part<T>? = null)

class PartTestRunner<T: Any>(
    private val inputParser: InputParser<T>,
) {

    private val tests: MutableList<Test<T>> = mutableListOf()

    infix fun String.shouldOutput(output: Any?) {
        tests.add(Test(this, output))
    }

    infix fun String.with(part: Part<T>) = this to part

    infix fun Pair<String, Part<T>>.shouldOutput(output: Any?) {
        tests.add(Test(first, output, second))
    }

    fun test(part: Part<T>) = getTestResults(part).forEachIndexed { index, (test, result) ->
        val testNumber = index + 1
        when (result) {
            is TestException -> printRed {
                println("Test $testNumber: ✖ Failed with an exception")
                result.cause.printStackTrace()
            }
            TestPassed -> printGreen {
                println("Test $testNumber: ✔ Passed")
            }
            is TestWrongOutput -> printRed {
                println("Test $testNumber: ✖ Wrong output")
                println(" ⇒ Got: ${result.output}")
                println(" ⇒ Expected: ${test.expectedValue}")
            }
        }
    }

    private fun getTestResults(part: Part<T>) = tests.map {
        try {
            val input = inputParser(it.input.lines())
            val result = if (it.part != null) {
                it.part.invoke(input)
            } else {
                part(input)
            }
            if (result.toString() == it.expectedValue.toString()) {
                it to TestPassed
            } else {
                it to TestWrongOutput(result)
            }
        } catch (error: Throwable) {
            it to TestException(error)
        }
    }
}