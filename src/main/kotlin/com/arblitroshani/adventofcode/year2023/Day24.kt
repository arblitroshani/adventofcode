package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.AocPuzzle
import com.arblitroshani.adventofcode.util.common.Point3D
import com.arblitroshani.adventofcode.util.common.Vector3D
import com.arblitroshani.adventofcode.util.extension.getAllPairs

typealias Input23d24 = List<Vector3D>

class Day24: AocPuzzle<Input23d24>() {

    private var validRange: LongRange = 7L .. 27L

    override fun parseInput(lines: List<String>): Input23d24 {
        if (lines.size > 5) validRange = 200000000000000 .. 400000000000000
        return lines.map {
            val v = it.split(",", "@").map(String::trim).map(String::toLong)
            Vector3D(Point3D(v[0], v[1], v[2]), v[3].toInt(), v[4].toInt(), v[5].toInt())
        }
    }

    override fun partOne(): Int =
        input.getAllPairs()
            .map { (h1, h2) ->
                val intersection = h1.line.intersectionPointTo(h2.line) ?: return@map false

                val xIsValid = intersection.x >= validRange.first && intersection.x <= validRange.last
                val yIsValid = intersection.y >= validRange.first && intersection.y <= validRange.last
                if (!(xIsValid && yIsValid)) return@map false

                h1.isInFuture(intersection) && h2.isInFuture(intersection)
            }.count { it }

    override fun partTwo(): Int = 0 // TODO
}

fun main() = Day24().solve(
    expectedAnswerForSampleInP1 = 2,
//    expectedAnswerForSampleInP2 = 47,
)
