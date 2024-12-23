package util.common.algorithms

fun <T> List<T>.combinations(size: Int): List<List<T>> {
    if (size == 0) return listOf(emptyList())
    if (this.isEmpty()) return emptyList()
    val sublist = drop(1)
    val withElement = sublist.combinations(size - 1).map { it + first() }
    val withoutElement = sublist.combinations(size)
    return withElement + withoutElement
}
