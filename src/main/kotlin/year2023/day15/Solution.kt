package year2023.day15

import framework.solution

private typealias Input = List<String>

fun main() = solution<Input>(2023, 15) {

    fun hash(s: String): Int {
        var cv = 0
        s.forEach {
            cv += it.code
            cv *= 17
            cv %= 256
        }
        return cv
    }

    parseInput { lines ->
        lines.first().split(",")
    }

    partOne { input ->
        input.sumOf(::hash)
    }

    partTwo { input ->
        data class Element(val name: String, var focalLength: Int)

        val slots = Array(256) { mutableListOf<Element>() }

        input.map { line ->
            val label: String
            var v: Int? = null

            if (line.endsWith('-'))
                label = line.dropLast(1)
            else {
                val parts = line.split("=")
                label = parts.first()
                v = parts.last().toInt()
            }

            val slot = slots[hash(label)]

            if (v == null) {
                slot.removeIf { it.name == label }
            } else if (slot.any { it.name == label}) {
                val index = slot.indexOfFirst { it.name == label }
                slot[index].focalLength = v
            } else {
                slot.add(Element(label, v))
            }
        }

        slots.withIndex().sumOf { (i, slot) ->
            slot.withIndex().sumOf { (j, lens) ->
                (i + 1) * (j + 1) * lens.focalLength
            }
        }
    }

    val testInput = """rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"""

    partOneTest {
        testInput shouldOutput 1320
    }

    partTwoTest {
        testInput shouldOutput 145
    }
}
