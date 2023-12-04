package year2022.day21

import java.io.File

class InputParser {
    private val input = File("./src/main/kotlin/year2022/day21/input.txt").readLines()

    val completedTasks = mutableMapOf<String, Long>()
    val incompleteTasks = mutableMapOf<String, MonkeyTask>()

    fun parse() = input.forEach { line ->
        val (monkeyName, monkeyTask) = line.split(":").map(String::trim)
        if (monkeyTask.all(Char::isDigit)) {
            completedTasks[monkeyName] = monkeyTask.toLong()
        } else {
            val (firstMonkey, operation, secondMonkey) = monkeyTask.split(" ").map(String::trim)
            incompleteTasks[monkeyName] = MonkeyTask(firstMonkey, secondMonkey, MonkeyOperation.from(operation))
        }
    }
}
