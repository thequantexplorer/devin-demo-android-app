package com.acme.tipjar

import org.junit.Assert.assertEquals
import org.junit.Test

class TipCalculatorTest {

    @Test
    fun `15 percent of 50 dollars for one person`() {
        assertEquals(TipResult(tipCents = 750, totalCents = 5750, perPersonCents = 5750),
            TipCalculator.calculate(billCents = 5000, tipPercent = 15, people = 1))
    }

    @Test
    fun `tip rounds half up to the nearest cent`() {
        // 18% of $10.25 is 184.5 cents, which rounds up to 185.
        assertEquals(185, TipCalculator.calculate(billCents = 1025, tipPercent = 18, people = 1).tipCents)
    }

    @Test
    fun `split rounds each share up so the group never pays less than the total`() {
        // $100.00 at 20% is $120.00. Split 3 ways that is $40.00 each.
        assertEquals(4000, TipCalculator.calculate(billCents = 10000, tipPercent = 20, people = 3).perPersonCents)
        // $10.00 at 0% split 3 ways is 333.33 cents, so each person pays 334.
        assertEquals(334, TipCalculator.calculate(billCents = 1000, tipPercent = 0, people = 3).perPersonCents)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `zero people is rejected`() {
        TipCalculator.calculate(billCents = 1000, tipPercent = 15, people = 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `negative bill is rejected`() {
        TipCalculator.calculate(billCents = -1, tipPercent = 15, people = 1)
    }
}
