package year2024.day19

import framework.solution
import util.InputParsing

private data class Input(val root: TrieNode, val towels: List<String>)

private data class TrieNode(
    val nodes: MutableMap<Char, TrieNode> = mutableMapOf(),
    var isLeaf: Boolean = false,
)

fun main() = solution<Input>(2024, 19) {

    parseInput { lines ->
        val (patternLines, towelLines) = InputParsing.splitListByEmptyLines(lines)
        val root = TrieNode()
        patternLines.first()
            .split(", ")
            .filterNot(String::isEmpty)
            .forEach { pattern ->
                var currentTrieNode = root
                pattern.forEachIndexed { i, ch ->
                    val newNode = currentTrieNode.nodes.getOrPut(ch) { TrieNode() }
                    if (i == pattern.length - 1) newNode.isLeaf = true
                    currentTrieNode = newNode
                }
            }
        Input(root, towelLines)
    }

    fun getIndices(towel: String, root: TrieNode): List<Int> {
        val indices = mutableListOf<Int>()
        var trieNode = root
        var i = 0
        while (i < towel.length) {
            if (!trieNode.nodes.containsKey(towel[i])) break
            val newNode = trieNode.nodes.getOrElse(towel[i]) { error("") }
            if (newNode.isLeaf) indices.add(i + 1)
            i++
            trieNode = newNode
        }
        return indices
    }

    partOne { (root, towels) ->
        val memo = mutableMapOf<String, Boolean>()

        fun isValid(towel: String): Boolean =
            towel.isEmpty() || memo.getOrPut(towel) {
                return@getOrPut getIndices(towel, root).any {
                    isValid(towel.drop(it))
                }
            }

        towels.count(::isValid)
    }

    partTwo { (root, towels) ->
        val memo = mutableMapOf<String, Long>()

        fun countValid(towel: String): Long = when {
            towel.isEmpty() -> 1
            else -> memo.getOrPut(towel) {
                return@getOrPut getIndices(towel, root).sumOf {
                    countValid(towel.drop(it))
                }
            }
        }

        towels.sumOf(::countValid)
    }

    val testInput = """
        r, wr, b, g, bwu, rb, gb, br

        brwrr
        bggr
        gbbr
        rrbgbr
        ubwu
        bwurrg
        brgr
        bbrgwb
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 6
    }

    partTwoTest {
        testInput shouldOutput 16
    }
}
