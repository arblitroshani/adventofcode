package year2023.day7

data class CamelHand(
    val hand: Hand,
    val bid: Int,
): Comparable<CamelHand> {

    override fun compareTo(other: CamelHand): Int = hand.compareTo(other.hand)
}
