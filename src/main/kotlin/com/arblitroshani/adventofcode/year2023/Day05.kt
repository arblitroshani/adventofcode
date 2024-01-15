package com.arblitroshani.adventofcode.year2023

import com.arblitroshani.adventofcode.AocPuzzle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

data class SeedRange(
    val seedStart: Long,
    val numberOfSeeds: Long,
)

data class MapEntry(
    val destRangeStart: Long,
    val sourceRangeStart: Long,
    val rangeLength: Long,
) {
    companion object {
        fun from(line: String): MapEntry {
            val (first, second, third) = line.split(' ').map(String::toLong)
            return MapEntry(first, second, third)
        }
    }
}

data class Input23d05(
    val seeds: List<Long>,
    val seedRanges: List<SeedRange>,
    val maps: MutableList<List<MapEntry>>,
)

class Day05: AocPuzzle<Input23d05>() {

    override fun parseInput(lines: List<String>): Input23d05 {
        val maps: MutableList<List<MapEntry>> = mutableListOf()

        val newMap: MutableList<MapEntry> = mutableListOf()
        for (i in 1 until lines.size) {
            val line = lines[i]
            if (line.isBlank() || line.isEmpty()) continue
            if (line.contains(':')) {
                if (newMap.isNotEmpty()) maps.add(newMap.toList())
                newMap.clear()
                continue
            }
            newMap.add(MapEntry.from(line))
        }
        maps.add(newMap)

        val seeds: List<Long> = lines[0]
            .split(": ").last()
            .split(' ')
            .map(String::toLong)
        val seedRanges = seeds.chunked(2).map { SeedRange(it[0], it[1]) }

        return Input23d05(seeds, seedRanges, maps)
    }

    override fun partOne(): Long {
        val sources = input.seeds.toMutableList()
        input.maps.forEach { map ->
            (0 until sources.size).forEach { sources[it] = getDestination(sources[it], map) }
        }
        return sources.min()
    }

    override fun partTwo(): Long =
        runBlocking(Dispatchers.Default) {
            input.seedRanges.map { r ->
                async {
                    var smallestLocation = Long.MAX_VALUE
                    for (startSeed in r.seedStart ..< r.seedStart + r.numberOfSeeds) {
                        var source = startSeed
                        input.maps.forEach { source = getDestination(source, it) }
                        smallestLocation = minOf(smallestLocation, source)
                    }
                    smallestLocation
                }
            }.awaitAll().min()
        }

    private fun getDestination(source: Long, map: List<MapEntry>): Long {
        for (mapEntry in map) {
            val sRangeStart = mapEntry.sourceRangeStart
            val sRangeEnd = sRangeStart + mapEntry.rangeLength
            if (source in sRangeStart ..< sRangeEnd) return mapEntry.destRangeStart + (source - sRangeStart)
        }
        return source
    }
}

fun main() = Day05().solve(
    expectedAnswerForSampleInP1 = 35L,
    expectedAnswerForSampleInP2 = 46L,
)
