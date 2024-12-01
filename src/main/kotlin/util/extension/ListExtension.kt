package util.extension

fun List<Int>.decrementFirst(): List<Int> = toMutableList().apply { this[0]-- }

fun List<Int>.hasSingleZeroElement() = this == listOf(0)

fun List<String>.columns(): List<String> =
    (0 until first().length).map { j ->
        map { it[j] }.joinToString(separator = "")
    }

fun String.toIntList(): List<Int> = split(',').map(String::toInt)

fun <T> List<T>.getAllPairs(): List<Pair<T, T>> =
    flatMapIndexed { index, value ->
        subList(index + 1, size).map { other -> Pair(value, other) }
    }
