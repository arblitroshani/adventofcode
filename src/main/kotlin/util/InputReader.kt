package util

import java.io.File

class InputReader(
    private val year: Int,
    private val day: Int,
) {
    fun read(): List<String> =
        File("src/main/resources/input/$year/input$day.txt").readLines()
}
