package com.arblitroshani.adventofcode.util

import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

class InputReader(
    private val year: Int,
    private val day: Int,
) {
    private val mainUrlPath = "https://adventofcode.com/$year/day/$day"

    fun read(): List<String> {
        val inputFile = File("src/main/resources/input/$year/input$day.txt")
        if (inputFile.exists()) return inputFile.readLines()
        inputFile.parentFile.mkdirs()

        return (URL("$mainUrlPath/input").openConnection() as HttpURLConnection)
            .apply {
                val sessionToken = File("src/main/resources/auth/cookie").readText()
                requestMethod = "GET"
                setRequestProperty("Cookie", "session=$sessionToken")
            }
            .inputStream.reader()
            .use(InputStreamReader::readLines)
            .also { inputFile.writeText(it.joinToString("\n")) }
    }

    fun readSample(): List<String> {
        val sampleFile = File("src/main/resources/sample/$year/sample$day.txt")
        if (sampleFile.exists()) return sampleFile.readLines()
        sampleFile.parentFile.mkdirs()

        val rawHtmlContent = (URL(mainUrlPath).openConnection() as HttpURLConnection)
            .apply { requestMethod = "GET" }
            .inputStream.reader()
            .use(InputStreamReader::readLines)
            .joinToString("\n")

        return parseSample(rawHtmlContent)
            .also(sampleFile::writeText)
            .split("\n")
    }

    private fun parseSample(htmlContent: String): String {
        val lowercaseHtml = htmlContent.lowercase(Locale.getDefault())

        val patternsToMatchExampleStart = listOf("for example:", "example")
        var exampleIndex = -1
        for (pattern in patternsToMatchExampleStart) {
            exampleIndex = lowercaseHtml.indexOf(pattern)
            if (exampleIndex != -1) break
        }
        if (exampleIndex == -1) throw Exception("Parsing sample: Example not found.")

        val codeStartIndex = htmlContent.indexOf("<code>", startIndex = exampleIndex)
        if (codeStartIndex == -1) throw Exception("Parsing sample: '<code>' not found after 'example'")
        val codeEndIndex = htmlContent.indexOf("</code>", startIndex = codeStartIndex)

        return htmlContent.substring(codeStartIndex + "<code>".length, codeEndIndex).trim()
    }
}
