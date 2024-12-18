package util

import java.math.BigInteger

fun gcd(a: Long, b: Long): Long {
    if (b == 0L) return a
    return gcd(b, a % b)
}

fun lcm(a: Long, b: Long): Long {
    return a / gcd(a, b) * b
}

infix fun Long.pow(exp: Int): Long =
    BigInteger.valueOf(this).pow(exp).toLong()
