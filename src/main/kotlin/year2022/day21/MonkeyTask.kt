package year2022.day21

data class MonkeyTask(
    val firstMonkey: String,
    val secondMonkey: String,
    val operation: MonkeyOperation
)

fun MonkeyTask.result(completedTasks: Map<String, Long>): Long? {
    val firstResult = completedTasks[firstMonkey] ?: return null
    val secondResult = completedTasks[secondMonkey] ?: return null
    return when(operation) {
        MonkeyOperation.PLUS -> firstResult + secondResult
        MonkeyOperation.MINUS -> firstResult - secondResult
        MonkeyOperation.MULT -> firstResult * secondResult
        MonkeyOperation.DIV -> firstResult / secondResult
    }
}
