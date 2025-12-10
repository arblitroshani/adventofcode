package year2025.day10

import framework.solution
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.chocosolver.solver.Model
import util.extension.toIntList

private data class Machine(
    val lightDiagram: List<Char>,
    val buttons: List<List<Int>>,
    val joltages: List<Int>,
)

fun main() = solution<List<Machine>>(2025, 10) {

    parseInput { lines ->
        lines.map { line ->
            val parts = line.split(' ')
            Machine(
                lightDiagram = parts[0].removeSurrounding("[", "]").toList(),
                buttons = parts.subList(1, parts.size - 1).map { part ->
                    part.removeSurrounding("(", ")").toIntList()
                },
                joltages = parts.last().removeSurrounding("{", "}").toIntList(),
            )
        }
    }

    partOne { input ->
        fun fewestButtonsRequired(lightDiagram: List<Char>, buttons: List<List<Int>>): Int {
            var currentMin = 1000

            fun selectButtons(currentLightDiagram: List<Char>, selectedButtons: Set<List<Int>>, currentIndex: Int) {
                if (currentLightDiagram == lightDiagram) {
                    currentMin = minOf(currentMin, selectedButtons.size)
                    return
                }
                if (currentIndex >= buttons.size) return

                // take
                val updatedLightDiagram = currentLightDiagram.toMutableList()
                val button = buttons[currentIndex]
                button.forEach { index ->
                    updatedLightDiagram[index] = if (currentLightDiagram[index] == '#') '.' else '#'
                }
                selectButtons(updatedLightDiagram, selectedButtons.plus(setOf(button)), currentIndex + 1)

                // no take
                selectButtons(currentLightDiagram, selectedButtons, currentIndex + 1)
            }

            val emptyLightDiagram = List(lightDiagram.size) { '.' }
            selectButtons(emptyLightDiagram, emptySet(), 0)
            return currentMin
        }

        input.sumOf { (lightDiagram, buttons, _) ->
            fewestButtonsRequired(lightDiagram, buttons)
        }
    }

    partTwo { input ->
        fun fewestButtonsRequired(buttons: List<List<Int>>, joltages: List<Int>): Int {
            val model = Model("Linear eq system")

            val variables = Array(buttons.size) {
                model.intVar("x$it", 0, 99999)
            }

            val coefficients = joltages.indices.map { index ->
                buttons.map { if (it.contains(index)) 1 else 0 }.toIntArray()
            }

            coefficients.zip(joltages).forEach { (coefficient, joltage) ->
                model.scalar(variables, coefficient, "=", joltage)
                    .post()
            }

            val sumVar = model.intVar("sum", 0, 99999)
            model.sum(variables, "=", sumVar).post()

            val solution = model.solver.findOptimalSolution(sumVar, false)
                ?: error("No natural solution exists")
            return variables.sumOf(solution::getIntVal)
        }

        runBlocking(Dispatchers.Default) {
            input
                .map { (_, buttons, joltages) ->
                    async { fewestButtonsRequired(buttons, joltages) }
                }
                .awaitAll()
                .sum()
        }
    }

    val testInput = """
        [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
        [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
        [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 7
    }

    partTwoTest {
        testInput shouldOutput 33
    }
}
