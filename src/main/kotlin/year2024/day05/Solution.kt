package year2024.day05

import framework.solution
import util.InputParsing

private typealias OrderingRule = Pair<Int, Int>
private typealias Update = MutableList<Int>
private data class Input(val rules: List<OrderingRule>, val updates: List<Update>)

fun main() = solution<Input>(2024, 5) {

    fun Update.isCorrect(rules: List<OrderingRule>): Boolean =
        windowed(2)
            .map { (a, b) -> a to b }
            .all(rules::contains)

    fun Update.middle(): Int = this[size / 2]

    parseInput { lines ->
        val (orderingRulesLines, updatesLines) = InputParsing.splitListByEmptyLines(lines)
        Input(
            rules = orderingRulesLines.map { line ->
                val (n1, n2) = line.split('|').map(String::toInt)
                n1 to n2
            },
            updates = updatesLines.map { line ->
                line.split(',').map(String::toInt).toMutableList()
            },
        )
    }

    partOne { (rules, updates) ->
        updates
            .filter { it.isCorrect(rules) }
            .sumOf(Update::middle)
    }

    partTwo { (rules, updates) ->
        fun Update.fixed(): Update {
            val (i, j) = windowed(2)
                .map { (a, b) -> b to a }
                .firstOrNull(rules::contains) ?: return this
            remove(i)
            add(indexOf(j), i)
            return fixed()
        }

        updates
            .filterNot { it.isCorrect(rules) }
            .map(Update::fixed)
            .sumOf(Update::middle)
    }

    val testInput = """
        47|53
        97|13
        97|61
        97|47
        75|29
        61|13
        75|53
        29|13
        97|29
        53|29
        61|53
        97|53
        61|29
        47|13
        75|47
        97|75
        47|61
        75|61
        47|29
        75|13
        53|13

        75,47,61,53,29
        97,61,53,29,13
        75,29,13
        75,97,47,61,53
        61,13,29
        97,13,75,29,47
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 143
    }

    partTwoTest {
        testInput shouldOutput 123
    }
}
