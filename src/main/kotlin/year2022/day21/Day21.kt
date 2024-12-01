package year2022.day21

import framework.Day
import framework.InputReader
import util.println

enum class MonkeyOperation(val value: String) {
    PLUS("+"),
    MINUS("-"),
    MULT("*"),
    DIV("/"),
    ;

    companion object {
        fun from(value: String) = MonkeyOperation.entries.find { it.value == value } ?: PLUS
    }
}

data class MonkeyTask(val firstMonkey: String, val secondMonkey: String, val operation: MonkeyOperation) {
    fun result(completedTasks: Map<String, Long>): Long? {
        val firstResult = completedTasks[firstMonkey] ?: return null
        val secondResult = completedTasks[secondMonkey] ?: return null
        return when(operation) {
            MonkeyOperation.PLUS -> firstResult + secondResult
            MonkeyOperation.MINUS -> firstResult - secondResult
            MonkeyOperation.MULT -> firstResult * secondResult
            MonkeyOperation.DIV -> firstResult / secondResult
        }
    }
}

class InputParser {
    private val input = InputReader(Day(2022, 21)).read()

    val completedTasks = mutableMapOf<String, Long>()
    val incompleteTasks = mutableMapOf<String, MonkeyTask>()

    fun parse() {
        completedTasks.clear()
        incompleteTasks.clear()

        input.forEach { line ->
            val (monkeyName, monkeyTask) = line.split(":").map(String::trim)
            if (monkeyTask.all(Char::isDigit)) {
                completedTasks[monkeyName] = monkeyTask.toLong()
            } else {
                val (firstMonkey, operation, secondMonkey) = monkeyTask.split(" ").map(String::trim)
                incompleteTasks[monkeyName] = MonkeyTask(firstMonkey, secondMonkey, MonkeyOperation.from(operation))
            }
        }
    }
}

fun main() {
    val ip = InputParser().also(InputParser::parse)

    // Part 1
    while (ip.completedTasks["root"] == null)
        for ((monkeyName, task) in ip.incompleteTasks)
            task.result(ip.completedTasks)?.let {
                ip.completedTasks[monkeyName] = it
            }
    ip.completedTasks["root"].println()

    // Part 2
    ip.parse()

    ip.completedTasks.remove("humn") // so that it is not considered

    val tasksToComplete = mutableListOf("")
    while (tasksToComplete.isNotEmpty()) { // simplify
        tasksToComplete.clear()
        for ((monkeyName, task) in ip.incompleteTasks)
            task.result(ip.completedTasks)?.let {
                ip.completedTasks[monkeyName] = it
                tasksToComplete.add(monkeyName)
            }
        tasksToComplete.forEach(ip.incompleteTasks::remove)
    }

    var monkeyToCalculate = "root"
    while (ip.incompleteTasks.isNotEmpty()) { // solve backwards
        val m = ip.incompleteTasks[monkeyToCalculate]!!
        val toCalcNext = if (ip.completedTasks[m.firstMonkey] == null) m.firstMonkey else m.secondMonkey
        val valueToUse = ip.completedTasks[m.firstMonkey] ?: ip.completedTasks[m.secondMonkey]!!

        if (monkeyToCalculate == "root") {
            ip.completedTasks[toCalcNext] = valueToUse
        } else {
            val targetResult = ip.completedTasks[monkeyToCalculate]!!
            ip.completedTasks[toCalcNext] = when (m.operation) {
                MonkeyOperation.PLUS -> targetResult - valueToUse
                MonkeyOperation.MINUS -> valueToUse + if (toCalcNext == m.firstMonkey) targetResult else -targetResult
                MonkeyOperation.MULT -> targetResult / valueToUse
                MonkeyOperation.DIV -> valueToUse * if (toCalcNext == m.firstMonkey) targetResult else 1 / targetResult
            }
        }
        ip.incompleteTasks.remove(monkeyToCalculate)
        monkeyToCalculate = toCalcNext
    }

    ip.completedTasks["humn"].println()
}
