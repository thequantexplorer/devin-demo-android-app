package com.acme.tipjar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TipCalculatorTest {

    @Test
    fun `example bill of 42_50 at 18 percent split two ways`() {
        val result = TipCalculator.calculate("42.50", tipPercent = 18, people = 2)
        assertEquals(765L, result.tipCents)
        assertEquals(5015L, result.totalCents)
        assertEquals(2508L, result.perPersonCents)
        assertEquals("$7.65", TipCalculator.formatDollars(result.tipCents))
        assertEquals("$50.15", TipCalculator.formatDollars(result.totalCents))
        assertEquals("$25.08", TipCalculator.formatDollars(result.perPersonCents))
    }

    @Test
    fun `parses bill input into cents`() {
        assertEquals(4250L, TipCalculator.parseBillCents("42.50"))
        assertEquals(4250L, TipCalculator.parseBillCents("42.5"))
        assertEquals(4200L, TipCalculator.parseBillCents("42"))
        assertEquals(4200L, TipCalculator.parseBillCents("42."))
        assertEquals(50L, TipCalculator.parseBillCents(".5"))
        assertEquals(5L, TipCalculator.parseBillCents("0.05"))
        assertEquals(0L, TipCalculator.parseBillCents("0"))
        assertEquals(4250L, TipCalculator.parseBillCents(" 42.50 "))
        assertEquals(4250L, TipCalculator.parseBillCents("42,50"))
        assertEquals(99_999_999_999L, TipCalculator.parseBillCents("999999999.99"))
    }

    @Test
    fun `rejects empty and non numeric bill input`() {
        listOf("", " ", ".", "abc", "4a", "1.2.3", "1.234", "-5", "+5", "$5", "1e3", "1 000", "1234567890")
            .forEach { assertNull("expected null for \"$it\"", TipCalculator.parseBillCents(it)) }
    }

    @Test
    fun `empty or invalid bill gives zero for everything`() {
        listOf("", "abc", "12.345", "-1").forEach { input ->
            assertEquals(TipCalculator.Result.ZERO, TipCalculator.calculate(input, tipPercent = 18, people = 3))
        }
        assertEquals("$0.00", TipCalculator.formatDollars(TipCalculator.Result.ZERO.perPersonCents))
    }

    @Test
    fun `tip is exact when there is no fraction of a cent`() {
        assertEquals(765L, TipCalculator.tipCents(4250, 18))
        assertEquals(1500L, TipCalculator.tipCents(10000, 15))
        assertEquals(2000L, TipCalculator.tipCents(10000, 20))
        assertEquals(0L, TipCalculator.tipCents(0, 20))
    }

    @Test
    fun `tip rounds half up to the nearest cent`() {
        // 12.30 * 15% = 1.845 -> 1.85 (half-even would give 1.84)
        assertEquals(185L, TipCalculator.tipCents(1230, 15))
        // 0.30 * 15% = 0.045 -> 0.05
        assertEquals(5L, TipCalculator.tipCents(30, 15))
        // 10.10 * 15% = 1.515 -> 1.52
        assertEquals(152L, TipCalculator.tipCents(1010, 15))
    }

    @Test
    fun `tip rounds down below half a cent`() {
        // 10.03 * 15% = 1.5045 -> 1.50
        assertEquals(150L, TipCalculator.tipCents(1003, 15))
        // 0.01 * 18% = 0.0018 -> 0.00
        assertEquals(0L, TipCalculator.tipCents(1, 18))
        // 33.33 * 18% = 5.9994 -> 6.00
        assertEquals(600L, TipCalculator.tipCents(3333, 18))
    }

    @Test
    fun `total is bill plus tip`() {
        val result = TipCalculator.calculate(billCents = 1230, tipPercent = 15, people = 1)
        assertEquals(1415L, result.totalCents)
        assertEquals(1415L, result.perPersonCents)
    }

    @Test
    fun `each share rounds up to the next cent`() {
        assertEquals(2508L, TipCalculator.perPersonCents(5015, 2))
        assertEquals(3334L, TipCalculator.perPersonCents(10000, 3))
        assertEquals(1L, TipCalculator.perPersonCents(1, 7))
    }

    @Test
    fun `even split is not rounded up`() {
        assertEquals(2500L, TipCalculator.perPersonCents(5000, 2))
        assertEquals(5015L, TipCalculator.perPersonCents(5015, 1))
        assertEquals(0L, TipCalculator.perPersonCents(0, 4))
    }

    @Test
    fun `group never pays less than the total`() {
        for (total in 0L..2_000L) {
            for (people in 1..12) {
                val each = TipCalculator.perPersonCents(total, people)
                assertTrue(each * people >= total)
                assertTrue("overpaid by a cent or more per person", (each - 1) * people < total || each == 0L)
            }
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `people below one is rejected`() {
        TipCalculator.perPersonCents(1000, 0)
    }

    @Test
    fun `formats cents as US dollars with two decimals`() {
        assertEquals("$0.00", TipCalculator.formatDollars(0))
        assertEquals("$0.05", TipCalculator.formatDollars(5))
        assertEquals("$0.50", TipCalculator.formatDollars(50))
        assertEquals("$1.00", TipCalculator.formatDollars(100))
        assertEquals("$7.65", TipCalculator.formatDollars(765))
        assertEquals("$25.08", TipCalculator.formatDollars(2508))
        assertEquals("$999.99", TipCalculator.formatDollars(99_999))
        assertEquals("$1,000.00", TipCalculator.formatDollars(100_000))
        assertEquals("$1,234,567.89", TipCalculator.formatDollars(123_456_789))
        assertEquals("-$7.65", TipCalculator.formatDollars(-765))
    }
}
