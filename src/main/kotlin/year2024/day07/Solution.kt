package year2024.day07

import framework.solution

private data class Equation(val testValue: Long, val nums: List<Long>)

fun main() = solution<List<Equation>>(2024, 7) {

    fun concatenate(n1: Long, n2: Long): Long {
        if (n2 < 10) return n1 * 10 + n2
        if (n2 < 100) return n1 * 100 + n2
        if (n2 < 1000) return n1 * 1000 + n2
        error("Add one more, $n1 $n2")
    }

    fun Equation.canBeMadeTrue(useConcat: Boolean): Boolean =
        when (nums.size) {
            1 -> testValue == nums[0]
            else -> listOfNotNull(
                nums[0] + nums[1],
                nums[0] * nums[1],
                if (useConcat) concatenate(nums[0], nums[1]) else null
            )
                .filter { it <= testValue }
                .map { copy(nums = listOf(it) + nums.drop(2)) }
                .any { it.canBeMadeTrue(useConcat) }
        }

    parseInput { lines ->
        lines.map {
            val (tv, nums) = it.split(": ")
            Equation(
                testValue = tv.toLong(),
                nums = nums.split(' ').map(String::toLong),
            )
        }
    }

    partOne { equations ->
        equations
            .filter { it.canBeMadeTrue(false) }
            .sumOf(Equation::testValue)
    }

    partTwo { equations ->
        equations
            .filter { it.canBeMadeTrue(true) }
            .sumOf(Equation::testValue)
    }

    val testInput = """
        190: 10 19
        3267: 81 40 27
        83: 17 5
        156: 15 6
        7290: 6 8 6 15
        161011: 16 10 13
        192: 17 8 14
        21037: 9 7 18 13
        292: 11 6 16 20
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 3749
    }

    partTwoTest {
        testInput shouldOutput 11387
    }
}
