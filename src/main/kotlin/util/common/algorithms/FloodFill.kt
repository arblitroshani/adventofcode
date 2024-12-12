package util.common.algorithms

import util.common.CellIndex
import util.common.get
import util.common.set
import java.util.LinkedList
import java.util.Queue

/**
 * Fill the terrain index and all its same colored adjacent cells
 */
fun <T> floodFill(terrain: Array<Array<T>>, index: CellIndex, newColor: (T) -> T) {
    val q: Queue<CellIndex> = LinkedList()
    val visited = Array(terrain.size) { Array(terrain[0].size) { false } }
    val color = terrain[index]
    q.offer(index)
    while (q.isNotEmpty()) {
        val current = q.poll()
        if (visited[current] || terrain[current] != color) { continue }
        visited[current] = true
        terrain[current] = newColor(color)
        current.neighbors
            .filterNot { it.isOutsideBoundsOf(terrain) }
            .forEach(q::add)
    }
}
