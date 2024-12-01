package com.arblitroshani.adventofcode.year2023.day05

import com.arblitroshani.adventofcode.framework.solution
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

data class Input(
    val seeds: List<Long>,
    val seedRanges: List<SeedRange>,
    val maps: MutableList<List<MapEntry>>,
)

data class SeedRange(val seedStart: Long, val numberOfSeeds: Long)

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

fun main() = solution<Input>(2023, 5) {

    fun getDestination(source: Long, map: List<MapEntry>): Long {
        for (mapEntry in map) {
            val sRangeStart = mapEntry.sourceRangeStart
            val sRangeEnd = sRangeStart + mapEntry.rangeLength
            if (source in sRangeStart ..< sRangeEnd) return mapEntry.destRangeStart + (source - sRangeStart)
        }
        return source
    }

    parseInput { lines ->
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

        Input(seeds, seedRanges, maps)
    }

    partOne { input ->
        val sources = input.seeds.toMutableList()
        input.maps.forEach { map ->
            (0 until sources.size).forEach { sources[it] = getDestination(sources[it], map) }
        }
        sources.min()
    }

    partTwo { input ->
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
    }

    val testInput = """
        seeds: 79 14 55 13

        seed-to-soil map:
        50 98 2
        52 50 48

        soil-to-fertilizer map:
        0 15 37
        37 52 2
        39 0 15

        fertilizer-to-water map:
        49 53 8
        0 11 42
        42 0 7
        57 7 4

        water-to-light map:
        88 18 7
        18 25 70

        light-to-temperature map:
        45 77 23
        81 45 19
        68 64 13

        temperature-to-humidity map:
        0 69 1
        1 0 69

        humidity-to-location map:
        60 56 37
        56 93 4
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 35
    }

    partTwoTest {
        testInput shouldOutput 46
    }
}
