package year2023.day7

val memo = mutableMapOf<String, Int>()

data class Hand(
    val cards: List<Int>,
): Comparable<Hand> {

    private val type: Int

    init {
        val groupedCards = cards.groupBy { it }
        val firstElementSize = groupedCards[cards[0]]?.size

        val isFiveOfKind  = groupedCards.size == 1 && firstElementSize == 5
        val isFourOfKind  = groupedCards.size == 2 && (firstElementSize == 1 || firstElementSize == 4)
        val isFullHouse   = groupedCards.size == 2 && (firstElementSize == 2 || firstElementSize == 3)
        val isThreeOfKind = groupedCards.maxOf { (_, v) -> v.size } == 3
        val isTwoPair     = groupedCards.size == 3 && (firstElementSize == 2 || groupedCards[cards[1]]?.size == 2)
        val isOnePair     = groupedCards.maxOf { (_, v) -> v.size } == 2
        val highCard      = groupedCards.maxOf { (_, v) -> v.size } == 1

        type = if (isFiveOfKind) 6
        else if (isFourOfKind) 5
        else if (isFullHouse) 4
        else if (isThreeOfKind) 3
        else if (isTwoPair) 2
        else if (isOnePair) 1
        else if (highCard) 0
        else -1
    }

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
        if (this.bestType() > other.bestType()) return 1
        if (this.bestType() < other.bestType()) return -1
        for (i in 0 .. 4)
            if (this.cards[i] > other.cards[i]) return 1
            else if (this.cards[i] < other.cards[i]) return -1
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
