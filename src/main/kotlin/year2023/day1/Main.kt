package year2023.day1

import java.io.File

fun main() {
    val input = File("./src/main/kotlin/year2023/day1/input.txt").readLines()
    println(firstPart(input))
    println(secondPart(input))
}

fun firstPart(input: List<String>): Int = input.sumOf { line ->
    var digits = line.filter { it.isDigit() }
    if (digits.count() == 1) {
        digits = "$digits$digits"
    } else if (digits.count() > 2) {
        digits = "${digits.first()}${digits.last()}"
    }
    digits.toInt()
}

fun secondPart(input: List<String>): Int {
    val spelledNumbers = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    return input.sumOf { line ->
        val d1 = findFirstDigit(line, spelledNumbers)
        val d2 = findFirstDigit(line.reversed(), spelledNumbers.map { it.reversed() })
        "$d1$d2".toInt()
    }
}

private fun findFirstDigit(line: String, spelledNumbers: List<String>): String {
    var digit = ""
    var index = 0

    while (digit.isEmpty()) {
        if (line[index].isDigit()) {
            digit = line[index].toString()
            break
        }
        val substringToCheck = line.substring(index, (index + 5).coerceAtMost(line.length - 1))
        for (spelledNumber in spelledNumbers)
            if (substringToCheck.startsWith(spelledNumber)) {
                digit = "${spelledNumbers.indexOf(spelledNumber) + 1}"
                break
            }
        index += 1
    }
    return digit
}
