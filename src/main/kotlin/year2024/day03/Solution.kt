package year2024.day03

import framework.solution

fun main() = solution(2024, 3) {

    val mulPattern = "mul\\(\\d{1,3},\\d{1,3}\\)"

    fun performMultiplication(match: String): Int {
        val parts = match.split("mul(", ",", ")")
        return parts[1].toInt() * parts[2].toInt()
    }

    parseInput { lines ->
        lines.joinToString()
    }

    partOne { input ->
        Regex(mulPattern)
            .findAll(input)
            .map(MatchResult::value)
            .sumOf(::performMultiplication)
    }

    partTwo { input ->
        val enablePattern = "do\\(\\)"
        val disablePattern = "don't\\(\\)"
        var isEnabled = true

        Regex("$mulPattern|$enablePattern|$disablePattern")
            .findAll(input)
            .map(MatchResult::value)
            .filter {
                if (it == "do()") {
                    isEnabled = true
                    return@filter false
                }
                if (it == "don't()") {
                    isEnabled = false
                    return@filter false
                }
                return@filter isEnabled
            }
            .sumOf(::performMultiplication)
    }

    partOneTest {
        "mul(44,46)" shouldOutput 2024
        "mul(123,4)" shouldOutput 492
        "mul ( 2 , 4 )" shouldOutput 0
        "mul(6,9!" shouldOutput 0
        "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))" shouldOutput 161
    }

    partTwoTest {
        "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))" shouldOutput 48
    }
}
