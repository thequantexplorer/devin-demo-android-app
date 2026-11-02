package com.acme.tipjar

import java.math.BigDecimal
import java.math.RoundingMode

/** Converts between whole cents and US dollar text. */
object Money {
    /** Formats cents as US dollars with two decimals, for example 765 -> "$7.65". */
    fun formatDollars(cents: Long): String {
        val sign = if (cents < 0) "-" else ""
        val abs = BigDecimal.valueOf(cents).abs()
        val dollars = abs.divideToIntegralValue(BigDecimal.valueOf(100)).toBigInteger()
        val remainder = abs.remainder(BigDecimal.valueOf(100)).toInt()
        val grouped = dollars.toString().reversed().chunked(3).joinToString(",").reversed()
        return "$sign$$grouped.${remainder.toString().padStart(2, '0')}"
    }

    /**
     * Parses a bill typed by the user, for example "42.50" -> 4250.
     * Returns null if the text is empty, not a number, negative or too large.
     * Extra decimal places are rounded half up to the nearest cent.
     */
    fun parseDollars(text: String): Long? {
        val amount = text.trim().toBigDecimalOrNull() ?: return null
        if (amount.signum() < 0) return null
        return try {
            amount.setScale(2, RoundingMode.HALF_UP).movePointRight(2).longValueExact()
        } catch (e: ArithmeticException) {
            null
        }
    }
}
