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
        TODO("Not implemented yet. See docs/specs/tip-calculator.md")
    }
}
