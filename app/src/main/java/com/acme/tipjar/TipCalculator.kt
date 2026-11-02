package com.acme.tipjar

data class TipResult(
    val tipCents: Long,
    val totalCents: Long,
    val perPersonCents: Long,
)

/**
 * Tip math for the tip calculator screen. See docs/specs/tip-calculator.md.
 * Money is in whole cents so there are no floating-point rounding errors.
 */
object TipCalculator {
    fun calculate(billCents: Long, tipPercent: Int, people: Int): TipResult {
        require(billCents >= 0) { "billCents must not be negative, was $billCents" }
        require(tipPercent >= 0) { "tipPercent must not be negative, was $tipPercent" }
        require(people >= 1) { "people must be at least 1, was $people" }

        val tipCents = (Math.multiplyExact(billCents, tipPercent.toLong()) + 50) / 100
        val totalCents = Math.addExact(billCents, tipCents)
        val perPersonCents = (totalCents + people - 1) / people
        return TipResult(tipCents, totalCents, perPersonCents)
    }
}
