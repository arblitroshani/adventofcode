package util

private const val ansiRed = "\u001B[31m"
private const val ansiGreen = "\u001B[32m"
private const val ansiYellow = "\u001B[33m"
private const val ansiReset = "\u001B[0m"

fun printRed(function: () -> Unit) = printColor(ansiRed, function)
fun printGreen(function: () -> Unit) = printColor(ansiGreen, function)
fun printYellow(function: () -> Unit) = printColor(ansiYellow, function)

private inline fun printColor(color: String, function: () -> Unit) {
    print(color)
    function()
    print(ansiReset)
}

fun Any?.println() = println(this)
fun Any?.print() = print(this)

fun Pair<Any, Any>.println() {
    first.println()
    second.println()
}
