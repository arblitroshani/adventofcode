package year2023.day5

fun main() {
    val ip = InputParser().also(InputParser::parse)

    val sources = ip.seeds.toMutableList()
    ip.maps.forEach { map ->
        (0 until sources.size).forEach { sources[it] = getDestination(sources[it], map) }
    }
    println("Solution pt1: ${sources.min()}")

    var smallestLocation = Long.MAX_VALUE
    ip.seedRanges.forEach { r ->
        for (startSeed in r.seedStart..<(r.seedStart + r.numberOfSeeds)) {
            var source = startSeed
            ip.maps.forEach { source = getDestination(source, it) }
            smallestLocation = minOf(smallestLocation, source)
        }
    }
    println("Solution pt2: $smallestLocation")
}

fun getDestination(source: Long, map: List<MapEntry>): Long {
    for (mapEntry in map) {
        val sRangeStart = mapEntry.sourceRangeStart
        val sRangeEnd = sRangeStart + mapEntry.rangeLength
        if (source in sRangeStart ..< sRangeEnd) return mapEntry.destRangeStart + (source - sRangeStart)
    }
    return source
}
