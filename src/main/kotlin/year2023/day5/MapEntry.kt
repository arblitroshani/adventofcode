package year2023.day5

data class MapEntry(
    val destRangeStart: Long,
    val sourceRangeStart: Long,
    val rangeLength: Long,
) {
    companion object {
        fun create(line: String): MapEntry {
            val (first, second, third) = line.split(" ").map(String::toLong)
            return MapEntry(
                destRangeStart = first,
                sourceRangeStart = second,
                rangeLength = third,
            )
        }
    }
}
