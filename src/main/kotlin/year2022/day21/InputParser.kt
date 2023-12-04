package year2022.day21

import java.io.File

class InputParser {
    private val input = File("./src/main/kotlin/year2022/day21/input.txt").readLines()

    val completedTasks = mutableMapOf<String, Long>()
    val incompleteTasks = mutableMapOf<String, MonkeyTask>()

    fun parse() {
        completedTasks.clear()
        incompleteTasks.clear()

        input.forEach { line ->
            val monkeyName = line.split(":").first()
            val monkeyTask = line.split(":").last().trim()
            if (monkeyTask.any { it in listOf('+', '-', '*', '/') }) {
                val taskParts = monkeyTask.split(" ").map(String::trim)
                incompleteTasks[monkeyName] = MonkeyTask(
                    firstMonkey = taskParts[0],
                    secondMonkey = taskParts[2],
                    operation = MonkeyOperation.from(taskParts[1])
                )
            } else {
                completedTasks[monkeyName] = monkeyTask.toLong()
            }
        }
    }
}
