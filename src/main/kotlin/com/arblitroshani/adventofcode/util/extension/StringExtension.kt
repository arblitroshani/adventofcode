package com.arblitroshani.adventofcode.util.extension

fun String.repeat(n: Int, joiner: String): String =
    generateSequence(this) { it }.take(n).joinToString(joiner)
