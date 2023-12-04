package year2022.day21

enum class MonkeyOperation(val value: String) {
    PLUS("+"),
    MINUS("-"),
    MULT("*"),
    DIV("/");

    companion object {
        fun from(value: String) = MonkeyOperation.entries.find { it.value == value } ?: PLUS
    }
}
