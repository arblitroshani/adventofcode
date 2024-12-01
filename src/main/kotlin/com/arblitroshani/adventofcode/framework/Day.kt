package com.arblitroshani.adventofcode.framework

import java.io.File
import java.net.URI
import java.net.URL

class Day(year: Int, day: Int) {

    private val paddedDay = day.toString().padStart(2, '0')

    private val mainUrlPath = "https://adventofcode.com/$year/day/$day"

    val inputUrl: URL = URI("$mainUrlPath/input").toURL()
    val directory = File("src/main/kotlin/com/arblitroshani/adventofcode/year$year/day$paddedDay")
    val inputFile = File(directory, "input.txt")

    fun solutionFile(part: Int) = File(directory, "part$part.txt")
}