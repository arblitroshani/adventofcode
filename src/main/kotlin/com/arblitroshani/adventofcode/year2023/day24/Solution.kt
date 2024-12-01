package com.arblitroshani.adventofcode.year2023.day24

import com.arblitroshani.adventofcode.framework.solution
import com.arblitroshani.adventofcode.util.common.Point3D
import com.arblitroshani.adventofcode.util.common.Vector3D
import com.arblitroshani.adventofcode.util.extension.getAllPairs

private typealias Input = List<Vector3D>

fun main() = solution<Input>(2023, 24) {

    parseInput { lines ->
        lines.map {
            val v = it.split(",", "@").map(String::trim).map(String::toLong)
            Vector3D(Point3D(v[0], v[1], v[2]), v[3].toInt(), v[4].toInt(), v[5].toInt())
        }
    }

    partOne { input ->
        var validRange: LongRange = 7L .. 27L
        if (input.size > 5) validRange = 200000000000000 .. 400000000000000

        input.getAllPairs()
            .map { (h1, h2) ->
                val intersection = h1.line.intersectionPointTo(h2.line) ?: return@map false

                val xIsValid = intersection.x >= validRange.first && intersection.x <= validRange.last
                val yIsValid = intersection.y >= validRange.first && intersection.y <= validRange.last
                if (!(xIsValid && yIsValid)) return@map false

                h1.isInFuture(intersection) && h2.isInFuture(intersection)
            }
            .count { it }
    }

    partOneTest {
        """
            19, 13, 30 @ -2,  1, -2
            18, 19, 22 @ -1, -1, -2
            20, 25, 34 @ -2, -2, -4
            12, 31, 28 @ -1, -2, -1
            20, 19, 15 @  1, -5, -3
        """.trimIndent() shouldOutput 2
    }
}
