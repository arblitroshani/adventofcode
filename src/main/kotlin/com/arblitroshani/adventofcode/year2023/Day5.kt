package com.arblitroshani.adventofcode.year2023

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import com.arblitroshani.adventofcode.util.InputReader
import com.arblitroshani.adventofcode.util.print

data class MapEntry(val destRangeStart: Long, val sourceRangeStart: Long, val rangeLength: Long) {
    companion object {
        fun from(line: String): MapEntry {
            val (first, second, third) = line.split(" ").map(String::toLong)
            return MapEntry(first, second, third)
        }
    }
}

data class SeedRange(val seedStart: Long, val numberOfSeeds: Long)

class InputParser {

    private val input = InputReader(2023, 5).read()

    var seeds: List<Long> = emptyList()
    var seedRanges: List<SeedRange> = emptyList()
    var maps: MutableList<List<MapEntry>> = mutableListOf()

    fun parse() {
        seeds = input[0]
            .split(": ").last()
            .split(' ')
            .map(String::toLong)

        val newMap: MutableList<MapEntry> = mutableListOf()
        for (i in 1 until input.size) {
            val line = input[i]
            if (line.isBlank() || line.isEmpty()) continue
            if (line.contains(':')) {
                if (newMap.isNotEmpty()) maps.add(newMap.toList())
                newMap.clear()
                continue
            }
            newMap.add(MapEntry.from(line))
        }
        maps.add(newMap)

        seedRanges = seeds.chunked(2).map { SeedRange(it[0], it[1]) }
    }
}

fun main() {
    val ip = InputParser().also(InputParser::parse)

    val sources = ip.seeds.toMutableList()
    ip.maps.forEach { map ->
        (0 until sources.size).forEach { sources[it] = getDestination(sources[it], map) }
    }
    sources.min().print()

    runBlocking(Dispatchers.Default) {
        ip.seedRanges.map { r ->
            async {
                var smallestLocation = Long.MAX_VALUE
                for (startSeed in r.seedStart ..< r.seedStart + r.numberOfSeeds) {
                    var source = startSeed
                    ip.maps.forEach { source = getDestination(source, it) }
                    smallestLocation = minOf(smallestLocation, source)
                }
                smallestLocation
            }
        }.awaitAll().min().print()
    }
}

fun getDestination(source: Long, map: List<MapEntry>): Long {
    for (mapEntry in map) {
        val sRangeStart = mapEntry.sourceRangeStart
        val sRangeEnd = sRangeStart + mapEntry.rangeLength
        if (source in sRangeStart ..< sRangeEnd) return mapEntry.destRangeStart + (source - sRangeStart)
    }
    return source
}
