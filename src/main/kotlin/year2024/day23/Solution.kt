package year2024.day23

import framework.solution
import util.common.algorithms.bfs
import util.common.algorithms.combinations

private data class Input(val al: Map<String, Set<String>>)

fun main() = solution<Input>(2024, 23) {

    fun traverse(input: Input, onNodeVisited: (String, List<String>) -> Unit) =
        bfs(
            start = input.al.keys.first(),
            adjacency = { node ->
                val neighbors = input.al[node]?.toList() ?: emptyList()
                onNodeVisited(node, neighbors)
                neighbors
            },
            isGoal = { false }
        )

    fun List<String>.areAllLinked(input: Input): Boolean =
        combinations(2).all { (a, b) -> input.al[a]?.contains(b) == true }

    parseInput { lines ->
        val adjacencyList = mutableMapOf<String, MutableSet<String>>()
        lines
            .map { it.split("-") }
            .forEach { (a, b) ->
                adjacencyList.getOrPut(a) { mutableSetOf() }.add(b)
                adjacencyList.getOrPut(b) { mutableSetOf() }.add(a)
            }
        Input(adjacencyList)
    }

    partOne { input ->
        val validTriples = mutableSetOf<List<String>>()
        traverse(input) { node, neighbors ->
            neighbors.combinations(2)
                .map { it + node }
                .filter { it.any { el -> el.startsWith('t') } && it.areAllLinked(input) }
                .forEach { validTriples.add(it.sorted()) }
        }
        validTriples.count()
    }

    partTwo { input ->
        val largestSet = mutableListOf<String>()
        traverse(input) { node, neighbors ->
            for (size in neighbors.size downTo largestSet.size) {
                val connectedSet = neighbors
                    .combinations(size)
                    .firstOrNull { it.areAllLinked(input) } ?: continue
                largestSet.apply {
                    clear()
                    add(node)
                    addAll(connectedSet)
                }
                break
            }
        }
        largestSet.sorted().joinToString(",")
    }

    val testInput = """
        kh-tc
        qp-kh
        de-cg
        ka-co
        yn-aq
        qp-ub
        cg-tb
        vc-aq
        tb-ka
        wh-tc
        yn-cg
        kh-ub
        ta-co
        de-co
        tc-td
        tb-wq
        wh-td
        ta-ka
        td-qp
        aq-cg
        wq-ub
        ub-vc
        de-ta
        wq-aq
        wq-vc
        wh-yn
        ka-de
        kh-ta
        co-tc
        wh-qp
        tb-vc
        td-yn
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 7
    }

    partTwoTest {
        testInput shouldOutput "co,de,ka,ta"
    }
}
