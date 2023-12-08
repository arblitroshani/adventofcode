package com.arblitroshani.adventofcode.util

fun gcd(a: Long, b: Long): Long {
    if (b == 0L) return a
    return gcd(b, a % b)
}

fun lcm(a: Long, b: Long): Long {
    return a / gcd(a, b) * b
}
