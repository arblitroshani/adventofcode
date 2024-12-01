package util

object InputParsing {

    fun splitListByEmptyLines(lines: List<String>): List<List<String>> =
        lines.fold(mutableListOf(mutableListOf<String>())) { acc, line ->
            if (line.isBlank()) {
                acc.add(mutableListOf())
            } else {
                if (acc.isEmpty()) acc.add(mutableListOf())
                acc.last().add(line)
            }
            acc
        }
}
