package com.arblitroshani.adventofcode.util

fun Any?.print() = println(this)

fun Pair<Any, Any>.print() {
    first.print()
    second.print()
}
