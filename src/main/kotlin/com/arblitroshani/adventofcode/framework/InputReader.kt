package com.arblitroshani.adventofcode.framework

import java.io.BufferedReader
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class InputReader(private val day: Day) {

    private val sessionToken by lazy {
        File("src/main/resources/auth/cookie")
            .also { require(it.exists()) { "Token file not found." } }
            .readText()
            .also { require(it.isNotBlank()) { "Token file is empty." } }
    }

    fun read(): List<String> {
        if (day.inputFile.exists()) return day.inputFile.readLines()
        day.inputFile.parentFile.mkdirs()

        return linesFromGetRequest(day.inputUrl).also {
            val content = it.joinToString("\n")
            day.inputFile.writeText(content)
        }
    }

    private fun linesFromGetRequest(url: URL): List<String> =
        url.openConnection()
            .let { it as HttpURLConnection }
            .apply {
                requestMethod = "GET"
                setRequestProperty("Cookie", "session=$sessionToken")
            }
            .inputStream
            .bufferedReader()
            .use(BufferedReader::readLines)
}
