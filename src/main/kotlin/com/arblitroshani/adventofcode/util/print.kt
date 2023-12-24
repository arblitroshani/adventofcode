package com.arblitroshani.adventofcode.util

private const val yellow = "\u001B[33m"
private const val red = "\u001B[31m"
private const val reset = "\u001B[0m"

fun Any?.println() = println(this)
fun Any?.print() = print(this)

fun Any?.printYellow() = print(yellow + this.toString() + reset)
fun Any?.printRed() = print(red + this.toString() + reset)

fun Pair<Any, Any>.println() {
    first.println()
    second.println()
}
