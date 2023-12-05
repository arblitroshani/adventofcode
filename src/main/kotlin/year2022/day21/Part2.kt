package year2022.day21

fun main() {
    val ip = InputParser().also(InputParser::parse)

    // MARK: - Remove humn so that it is not considered
    ip.completedTasks.remove("humn")

    // MARK: - Simplify
    val tasksToComplete = mutableListOf("")
    while (tasksToComplete.isNotEmpty()) {
        tasksToComplete.clear()
        for ((monkeyName, task) in ip.incompleteTasks)
            task.result(ip.completedTasks)?.let {
                ip.completedTasks[monkeyName] = it
                tasksToComplete.add(monkeyName)
            }
        tasksToComplete.forEach(ip.incompleteTasks::remove)
    }

    // MARK: - Solve backwards
    var monkeyToCalculate = "root"
    while (ip.incompleteTasks.isNotEmpty()) {
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

    println("Part2 solution: ${ip.completedTasks["humn"]}")
}
