package com.acme.tipjar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MoneyTest {

    @Test
    fun `zero formats as zero dollars`() {
        assertEquals("$0.00", Money.formatDollars(0))
    }

    @Test
    fun `cents always show two decimals`() {
        assertEquals("$0.05", Money.formatDollars(5))
        assertEquals("$1.00", Money.formatDollars(100))
        assertEquals("$7.65", Money.formatDollars(765))
        assertEquals("$25.08", Money.formatDollars(2508))
    }

    @Test
    fun `thousands are separated with commas`() {
        assertEquals("$999.99", Money.formatDollars(99_999))
        assertEquals("$1,000.00", Money.formatDollars(100_000))
        assertEquals("$1,234,567.89", Money.formatDollars(123_456_789))
    }

    @Test
    fun `negative amounts put the sign before the dollar sign`() {
        assertEquals("-$7.65", Money.formatDollars(-765))
    }

    @Test
    fun `typed bills parse to cents`() {
        assertEquals(4250L, Money.parseBillCents("42.50"))
        assertEquals(4250L, Money.parseBillCents("42.5"))
        assertEquals(4200L, Money.parseBillCents("42"))
        assertEquals(4200L, Money.parseBillCents("42."))
        assertEquals(50L, Money.parseBillCents(".5"))
    }

    @Test
    fun `empty or invalid bills parse to null`() {
        assertNull(Money.parseBillCents(""))
        assertNull(Money.parseBillCents("."))
        assertNull(Money.parseBillCents("abc"))
        assertNull(Money.parseBillCents("-5"))
        assertNull(Money.parseBillCents("1.005"))
        assertNull(Money.parseBillCents("1.2.3"))
    }

    @Test
    fun `bill input only accepts digits and up to two decimals`() {
        assertTrue(Money.isAcceptableBillInput(""))
        assertTrue(Money.isAcceptableBillInput("."))
        assertTrue(Money.isAcceptableBillInput("42.50"))
        assertFalse(Money.isAcceptableBillInput("42.505"))
        assertFalse(Money.isAcceptableBillInput("4a"))
        assertFalse(Money.isAcceptableBillInput("12345678"))
    }

    @Test
    fun `acceptance check from the spec`() {
        val result = TipCalculator.calculate(Money.parseBillCents("42.50")!!, tipPercent = 18, people = 2)
        assertEquals("$7.65", Money.formatDollars(result.tipCents))
        assertEquals("$50.15", Money.formatDollars(result.totalCents))
        assertEquals("$25.08", Money.formatDollars(result.perPersonCents))
    }
}
