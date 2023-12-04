package year2022.day21

fun main() {
    val ip = InputParser().also(InputParser::parse)

    while (ip.completedTasks["root"] == null)
        for ((monkeyName, task) in ip.incompleteTasks)
            task.result(ip.completedTasks)?.let {
                ip.completedTasks[monkeyName] = it
            }

    println("Part2 solution: ${ip.completedTasks["root"]}")
}
