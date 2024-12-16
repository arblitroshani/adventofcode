package util.common.algorithms

import java.util.PriorityQueue

/**
 * Dijkstra algorithm.
 * Returns first path that lead to minCost.
 */
fun <T> dijkstra(
    start: T,
    adjacency: (T) -> List<Pair<T, Long>>,
    isGoal: (T) -> Boolean,
): List<Pair<T, Long>> {

    fun <T> reconstructPath(end: T, pathToNode: Map<T, Pair<T, Long>>): List<Pair<T, Long>> {
        val path = mutableListOf<Pair<T, Long>>()
        var current = end
        while (pathToNode.containsKey(current)) {
            val previous = pathToNode[current]!!
            path.add(current to previous.second)
            current = previous.first
        }
        path.add(current to 0L)
        return path.reversed()
    }

    val openList = PriorityQueue(compareBy<Pair<T, Long>> { it.second })
    openList.add(start to 0L)

    val costOfShortestPathToNode = mutableMapOf(start to 0L)
    val pathToNode = mutableMapOf<T, Pair<T, Long>>()

    while (openList.isNotEmpty()) {
        val (currentNode, currentCost) = openList.poll()
        if (isGoal(currentNode)) return reconstructPath(currentNode, pathToNode)
        adjacency(currentNode).forEach { (nextNode, cost) ->
            val newCost = currentCost + cost
            val existingCost = costOfShortestPathToNode.getOrDefault(nextNode, Long.MAX_VALUE)
            if (newCost >= existingCost) return@forEach
            costOfShortestPathToNode[nextNode] = newCost
            pathToNode[nextNode] = currentNode to cost
            openList.add(nextNode to newCost)
        }
    }
    return emptyList() // no path found
}
