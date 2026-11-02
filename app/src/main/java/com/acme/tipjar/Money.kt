package com.acme.tipjar

/** Converts between whole cents and the US dollar text shown and typed on screen. */
object Money {
    /** Largest bill the screen accepts, in whole dollars. */
    const val MAX_BILL_DOLLAR_DIGITS = 7

    /** Formats cents as US dollars with two decimals and thousands separators, e.g. `$1,234.50`. */
    fun formatDollars(cents: Long): String {
        val sign = if (cents < 0) "-" else ""
        val abs = if (cents < 0) -cents else cents
        val dollars = (abs / 100).toString().reversed().chunked(3).joinToString(",").reversed()
        val rest = (abs % 100).toString().padStart(2, '0')
        return "$sign$$dollars.$rest"
    }

    /**
     * Parses a typed bill like `42.50`, `42.5` or `42` into cents.
     * Returns null when the text is empty, not a number, negative or has more than two decimals.
     */
    fun parseBillCents(text: String): Long? {
        val match = BILL_PATTERN.matchEntire(text.trim()) ?: return null
        val (whole, fraction) = match.destructured
        if (whole.isEmpty() && fraction.isEmpty()) return null
        val dollars = whole.ifEmpty { "0" }.toLong()
        val cents = fraction.padEnd(2, '0').toLong()
        return dollars * 100 + cents
    }

    /** True if [text] can still become a valid bill while the user is typing it. */
    fun isAcceptableBillInput(text: String): Boolean = text.isEmpty() || BILL_PATTERN.matches(text)

    private val BILL_PATTERN = Regex("""(\d{0,$MAX_BILL_DOLLAR_DIGITS})(?:\.(\d{0,2}))?""")
}
