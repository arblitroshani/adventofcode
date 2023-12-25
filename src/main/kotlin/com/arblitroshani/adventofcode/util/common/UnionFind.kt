package com.arblitroshani.adventofcode.util.common

class UnionFind(n: Int) {

    private val parent = IntArray(n) { it }
    private val rank = IntArray(n) { 0 }

    private fun find(x: Int): Int {
        if (parent[x] != x) parent[x] = find(parent[x])
        return parent[x]
    }

    fun union(x: Int, y: Int) {
        val xRoot = find(x)
        val yRoot = find(y)
        if (xRoot == yRoot) return

        if (rank[xRoot] < rank[yRoot]) {
            parent[xRoot] = yRoot
        } else if (rank[xRoot] > rank[yRoot]) {
            parent[yRoot] = xRoot
        } else {
            parent[yRoot] = xRoot
            rank[xRoot]++
        }
    }

    fun getDisjointSets(): List<Set<Int>> {
        val sets = HashMap<Int, MutableSet<Int>>()
        for (i in parent.indices)
            sets.computeIfAbsent(/* root */ find(i)) { HashSet() }.add(i)
        return sets.values.toList()
    }
}
