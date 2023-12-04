package year2022.day21

fun main() {
    val ip = InputParser().also(InputParser::parse)

    // MARK: - Add offset
    val humanValue = ip.completedTasks["humn"]!!
    ip.completedTasks.remove("humn")
    ip.completedTasks["humninit"] = humanValue
    ip.incompleteTasks["humn"] = MonkeyTask(
        firstMonkey = "humninit",
        secondMonkey = "offset",
        operation = MonkeyOperation.PLUS,
    )

    // MARK: - Simplify
    val tasksToComplete = mutableListOf("", "")
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
        val m1 = ip.incompleteTasks[monkeyToCalculate]!!.firstMonkey
        val m2 = ip.incompleteTasks[monkeyToCalculate]!!.secondMonkey
        val operation = ip.incompleteTasks[monkeyToCalculate]!!.operation
        ip.incompleteTasks.remove(monkeyToCalculate)

        val toCalcNext = if (ip.completedTasks[m1] == null) m1 else m2
        val valueToUse = ip.completedTasks[m1] ?: ip.completedTasks[m2]!!

        if (monkeyToCalculate == "root") {
            ip.completedTasks[toCalcNext] = valueToUse
        } else {
            val targetResult = ip.completedTasks[monkeyToCalculate]!!
            ip.completedTasks[toCalcNext] = when (operation) {
                MonkeyOperation.PLUS -> targetResult - valueToUse
                MonkeyOperation.MINUS -> if (toCalcNext == m1) targetResult + valueToUse else valueToUse - targetResult
                MonkeyOperation.MULT -> targetResult / valueToUse
                MonkeyOperation.DIV -> if (toCalcNext == m1) targetResult * valueToUse else valueToUse / targetResult
            }
        }
        monkeyToCalculate = toCalcNext
    }

    println("Part2 solution: ${ip.completedTasks["humn"]!!}")
}
