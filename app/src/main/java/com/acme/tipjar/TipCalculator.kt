package com.acme.tipjar

/**
 * Tip math in whole cents. Money is never held in Float or Double.
 */
object TipCalculator {

    val TIP_PERCENTS = listOf(15, 18, 20)
    const val DEFAULT_TIP_PERCENT = 18
    const val MIN_PEOPLE = 1
    const val MAX_PEOPLE = 99

    private const val MAX_WHOLE_DOLLAR_DIGITS = 9
    private val BILL_PATTERN = Regex("""^(\d*)(?:\.(\d{0,2}))?$""")

    data class Result(
        val tipCents: Long,
        val totalCents: Long,
        val perPersonCents: Long,
    ) {
        companion object {
            val ZERO = Result(tipCents = 0, totalCents = 0, perPersonCents = 0)
        }
    }

    /**
     * Parses a bill like "42.50", "42.5", "42" or ".5" into cents.
     * Accepts a comma as the decimal separator. Returns null when the
     * input is empty, not a number, negative, or has more than two decimals.
     */
    fun parseBillCents(input: String): Long? {
        val match = BILL_PATTERN.matchEntire(input.trim().replace(',', '.')) ?: return null
        val dollars = match.groupValues[1]
        val cents = match.groupValues[2]
        if (dollars.isEmpty() && cents.isEmpty()) return null
        if (dollars.trimStart('0').length > MAX_WHOLE_DOLLAR_DIGITS) return null
        val dollarCents = (dollars.ifEmpty { "0" }).toLong() * 100
        return dollarCents + cents.padEnd(2, '0').toLong()
    }

    /** Tip rounded half up to the nearest cent. */
    fun tipCents(billCents: Long, tipPercent: Int): Long {
        require(billCents >= 0) { "billCents must not be negative" }
        require(tipPercent >= 0) { "tipPercent must not be negative" }
        return (billCents * tipPercent + 50) / 100
    }

    /** Each share rounded up to the next cent, so the shares never add up to less than the total. */
    fun perPersonCents(totalCents: Long, people: Int): Long {
        require(totalCents >= 0) { "totalCents must not be negative" }
        require(people >= MIN_PEOPLE) { "people must be at least $MIN_PEOPLE" }
        return (totalCents + people - 1) / people
    }

    fun calculate(billCents: Long, tipPercent: Int, people: Int): Result {
        val tip = tipCents(billCents, tipPercent)
        val total = billCents + tip
        return Result(tipCents = tip, totalCents = total, perPersonCents = perPersonCents(total, people))
    }

    /** Returns [Result.ZERO] when the bill is empty or not a number. */
    fun calculate(billInput: String, tipPercent: Int, people: Int): Result {
        val billCents = parseBillCents(billInput) ?: return Result.ZERO
        return calculate(billCents, tipPercent, people)
    }

    /** Formats cents as US dollars, e.g. 765 -> "$7.65", 123456 -> "$1,234.56". */
    fun formatDollars(cents: Long): String {
        val sign = if (cents < 0) "-" else ""
        val abs = if (cents < 0) -cents else cents
        val dollars = (abs / 100).toString().reversed().chunked(3).joinToString(",").reversed()
        val remainder = (abs % 100).toString().padStart(2, '0')
        return "$sign$$dollars.$remainder"
    }
}
