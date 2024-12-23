package util.common.algorithms

import java.util.ArrayDeque

fun <T: Any> bfs(
    start: T,
    adjacency: (T) -> List<T>,
    isGoal: (T) -> Boolean,
): Set<T> {
    val openList = ArrayDeque<T>()
    openList.add(start)

    val visited = mutableSetOf<T>()

    while (openList.isNotEmpty()) {
        val currentNode = openList.poll()

        if (currentNode in visited) continue
        visited.add(currentNode)

        if (isGoal(currentNode)) break

        adjacency(currentNode)
            .filterNot(visited::contains)
            .forEach(openList::add)
    }
    return visited
}
