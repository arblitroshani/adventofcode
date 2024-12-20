package year2024.day19

import framework.solution
import util.InputParsing

private data class TrieNode(
    val nodes: MutableMap<Char, TrieNode> = mutableMapOf(),
    var isLeaf: Boolean = false,
)

fun main() = solution<Pair<TrieNode, List<String>>>(2024, 19) {

    val memo = mutableMapOf<String, Long>()

    fun getIndices(towel: String, root: TrieNode): List<Int> {
        val indices = mutableListOf<Int>()
        var trieNode = root
        var i = 0
        while (i < towel.length) {
            trieNode = trieNode.nodes
                .getOrElse(towel[i++]) { return indices }
                .also { if (it.isLeaf) indices.add(i) }
        }
        return indices
    }

    fun countValid(towel: String, root: TrieNode): Long = when {
        towel.isEmpty() -> 1
        else -> memo.getOrPut(towel) {
            getIndices(towel, root).sumOf { countValid(towel.drop(it), root) }
        }
    }

    parseInput { lines ->
        val (patternLines, towelLines) = InputParsing.splitListByEmptyLines(lines)
        val root = TrieNode()
        patternLines.first()
            .split(", ")
            .forEach { pattern ->
                pattern.foldIndexed(root) { i, acc, c ->
                    acc.nodes.getOrPut(c) { TrieNode() }.also {
                        if (i == pattern.length - 1) it.isLeaf = true
                    }
                }
            }
        root to towelLines
    }

    partOne { (root, towels) ->
        towels.map { countValid(it, root) }.count { it > 0 }
    }

    partTwo { (root, towels) ->
        memo.clear()
        towels.sumOf { countValid(it, root) }
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
