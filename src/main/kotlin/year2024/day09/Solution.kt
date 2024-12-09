package year2024.day09

import framework.solution

fun main() = solution<List<Int>>(2024, 9) {

    parseInput { lines ->
        lines.first().map { it - '0' }
    }

    partOne {
        val input = it.drop(1).toMutableList()

        var checksum = 0L
        var index = it.first()
        var stepIndex = 1

        while (input.size >= 2) {
            // fill spaces with values from right - decrease last
            for (i in 1..input.first()) {
                while (input.last() == 0) {
                    input.removeLast()
                    input.removeLast()
                    if (input.isEmpty()) break
                }
                if (input.isEmpty()) break
                checksum += index * ((input.size / 2) + stepIndex - 1)
                input[input.size - 1]--
                index++
            }

            if (input.isEmpty()) break
            input.removeFirst()

            // fill spaces with block values from left
            for (i in 1..input.first()) checksum += stepIndex * index++

            stepIndex++
            input.removeFirst()
        }
        checksum
    }

    partTwo { input ->
        data class File(val index: Int, val size: Int, var isMoved: Boolean)
        data class Space(var remaining: Int, val carriedFiles: MutableList<File>)

        val (spaces, files) = input.drop(1)
            .chunked(2)
            .mapIndexed { index, (s, f) ->
                val space = Space(remaining = s, carriedFiles = mutableListOf())
                val file = File(index = index + 1, size = f, isMoved = false)
                space to file
            }
            .unzip()

        files.reversed().forEach { file ->
            val firstFittingSpace = spaces
                .take(file.index)
                .firstOrNull { it.remaining >= file.size } ?: return@forEach
            firstFittingSpace.apply {
                remaining -= file.size
                carriedFiles.add(file)
                file.isMoved = true
            }
        }

        var checksum = 0L
        var index = input.first()

        spaces.zip(files) { space, file ->
            space.carriedFiles.forEach { carriedFile ->
                repeat(carriedFile.size) { checksum += carriedFile.index * index++ }
            }
            repeat(space.remaining) { index++ }
            repeat(file.size) {
                checksum += (if (file.isMoved) 0 else file.index) * index++
            }
        }
        checksum
    }

    val testInput = "2333133121414131402"

    partOneTest {
        testInput shouldOutput 1928
    }

    partTwoTest {
        testInput shouldOutput 2858
    }
}
