package com.arblitroshani.adventofcode.util.extension

fun String.repeat(n: Int, joiner: Char): String =
    (1..n).fold("") { acc, _ -> "$acc$joiner$this" }.drop(1)
