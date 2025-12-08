package year2025.day08

import framework.solution
import util.common.Point3D
import util.extension.getAllPairs
import util.pow

private data class Input(
    val points: List<Point3D>,
    val sortedPairs: List<Pair<Point3D, Point3D>>,
)

fun main() = solution<Input>(2025, 8) {

    parseInput { lines ->
        val points = lines.map {
            val (x, y, z) = it.split(",").map(String::toLong)
            Point3D(x, y, z)
        }
        val distances = points.getAllPairs().map { (a, b) ->
            val distSquared = ((a.x - b.x) pow 2) + ((a.y - b.y) pow 2) + ((a.z - b.z) pow 2)
            distSquared to (a to b)
        }
        Input(
            points = points,
            sortedPairs = distances.sortedBy { it.first }.map { it.second },
        )
    }

    fun connectPair(p1: Point3D, p2: Point3D, connectedSets: MutableList<Set<Point3D>>) {
        val (p1group, p2group) = listOf(p1, p2)
            .map { point -> connectedSets.firstOrNull { point in it } }
        listOf(p1group, p2group).forEach(connectedSets::remove)
        connectedSets.add((p1group ?: setOf(p1)) + (p2group ?: setOf(p2)))
    }

    partOne { (points, sortedDistances) ->
        val connectedSets = mutableListOf<Set<Point3D>>()
        val requiredDistances = sortedDistances.take(if (points.size == 20) 10 else 1000)
        for ((p1, p2) in requiredDistances) connectPair(p1, p2, connectedSets)
        connectedSets
            .sortedByDescending { it.size }
            .take(3)
            .fold(1L) { acc, list -> acc * list.size }
    }

    partTwo { (points, sortedDistances) ->
        val connectedSets = mutableListOf<Set<Point3D>>()
        for ((p1, p2) in sortedDistances) {
            connectPair(p1, p2, connectedSets)
            if (connectedSets.first().size == points.size) return@partTwo p1.x * p2.x
        }
    }

    val testInput = """
        162,817,812
        57,618,57
        906,360,560
        592,479,940
        352,342,300
        466,668,158
        542,29,236
        431,825,988
        739,650,466
        52,470,668
        216,146,977
        819,987,18
        117,168,530
        805,96,715
        346,949,466
        970,615,88
        941,993,340
        862,61,35
        984,92,344
        425,690,689
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 40
    }

    partTwoTest {
        testInput shouldOutput 25272
    }
}
