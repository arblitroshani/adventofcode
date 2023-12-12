package com.arblitroshani.adventofcode.util.extension

fun List<Int>.decrementFirst(): List<Int> = toMutableList().apply { this[0]-- }

fun List<Int>.hasSingleZeroElement() = this == listOf(0)

val List<Int>.memoKey: String get() = fold("") { acc, e -> "$acc.$e" }

fun String.toIntList(): List<Int> = split(',').map(String::toInt)