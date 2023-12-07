package year2023.day7

val memo = mutableMapOf<String, Int>()

data class Hand(
    val cards: List<Int>,
): Comparable<Hand> {

    private val groups = cards.groupBy { it }.map { it.value.size }.sortedDescending()

    private val type: Int =
        if (groups.size == 1)               6 // five of a kind
        else if (groups[0] == 4)            5 // four of a kind
        else if (groups == listOf(3, 2))    4 // full house
        else if (groups[0] == 3)            3 // three of a kind
        else if (groups == listOf(2, 2, 1)) 2 // two pair
        else if (groups[0] == 2)            1 // one pair
        else                                0 // high card

    private fun bestType(): Int {
        val memoKey = cards.sorted().fold("") { acc, card -> acc + "${card}_" }
        if (memo[memoKey] != null) return memo[memoKey]!!

        val bestType = cards
            .mapIndexed { i, card ->
                if (card != 1) return@mapIndexed type
                (2 .. 13)
                    .map { c ->
                        val cards = cards.toMutableList()
                        cards[i] = c
                        Hand(cards)
                    }
                    .maxOf(Hand::bestType)
            }
            .max()
        memo[memoKey] = bestType
        return bestType
    }

    override fun compareTo(other: Hand): Int {
        if (bestType() > other.bestType()) return 1
        if (bestType() < other.bestType()) return -1
        for (i in 0 .. 4)
            if (cards[i] > other.cards[i]) return 1
            else if (cards[i] < other.cards[i]) return -1
        return 0
    }

    companion object {
        fun create(hand: String, isPart2: Boolean = false): Hand {
            val cards = hand.map {
                if (it.isDigit()) return@map it.digitToInt()
                when (it) {
                    'T' -> 10
                    'J' -> if (isPart2) 1 else 11
                    'Q' -> if (isPart2) 11 else 12
                    'K' -> if (isPart2) 12 else 13
                    'A' -> if (isPart2) 13 else 14
                    else -> 0
                }
            }
            return Hand(cards)
        }
    }
}
