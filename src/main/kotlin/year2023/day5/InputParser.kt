package year2023.day5

import java.io.File

class InputParser {

    private val input = File("./src/main/kotlin/year2023/day5/input.txt").readLines()

    var seeds: List<Long> = emptyList()
    var seedRanges: List<SeedRange> = emptyList()
    var maps: MutableList<List<MapEntry>> = mutableListOf()

    fun parse() {
        seeds = input[0]
            .split(": ").last()
            .split(" ")
            .map(String::toLong)

        val newMap: MutableList<MapEntry> = mutableListOf()
        for (i in 1 until input.size) {
            val line = input[i]
            if (line.isBlank() || line.isEmpty()) continue
            if (line.contains(":")) {
                if (newMap.isNotEmpty()) maps.add(newMap.toList())
                newMap.clear()
                continue
            }
            newMap.add(MapEntry.create(line))
        }
        maps.add(newMap)

        seedRanges = seeds.chunked(2).map { SeedRange(it[0], it[1]) }
    }
}