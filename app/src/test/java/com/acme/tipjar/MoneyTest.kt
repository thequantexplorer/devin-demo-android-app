package com.acme.tipjar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MoneyTest {

    @Test
    fun `formats cents as dollars with two decimals`() {
        assertEquals("$7.65", Money.formatDollars(765))
        assertEquals("$50.15", Money.formatDollars(5015))
        assertEquals("$25.08", Money.formatDollars(2508))
    }

    @Test
    fun `formats zero and small amounts with leading zeros`() {
        assertEquals("$0.00", Money.formatDollars(0))
        assertEquals("$0.05", Money.formatDollars(5))
        assertEquals("$0.50", Money.formatDollars(50))
        assertEquals("$1.00", Money.formatDollars(100))
    }

    @Test
    fun `formats large amounts with thousands separators`() {
        assertEquals("$1,000.00", Money.formatDollars(100_000))
        assertEquals("$1,234,567.89", Money.formatDollars(123_456_789))
    }

    @Test
    fun `formats negative amounts with a leading minus`() {
        assertEquals("-$7.65", Money.formatDollars(-765))
    }

    @Test
    fun `parses typed bills into cents`() {
        assertEquals(4250L, Money.parseDollars("42.50"))
        assertEquals(4250L, Money.parseDollars("42.5"))
        assertEquals(4200L, Money.parseDollars("42"))
        assertEquals(4200L, Money.parseDollars("42."))
        assertEquals(50L, Money.parseDollars(".5"))
        assertEquals(1026L, Money.parseDollars("10.255"))
    }

    @Test
    fun `empty or invalid bills parse to null`() {
        assertNull(Money.parseDollars(""))
        assertNull(Money.parseDollars("   "))
        assertNull(Money.parseDollars("."))
        assertNull(Money.parseDollars("abc"))
        assertNull(Money.parseDollars("1.2.3"))
        assertNull(Money.parseDollars("-5"))
        assertNull(Money.parseDollars("1" + "0".repeat(30)))
    }

    @Test
    fun `acceptance check from the spec`() {
        val result = TipCalculator.calculate(Money.parseDollars("42.50")!!, tipPercent = 18, people = 2)
        assertEquals("$7.65", Money.formatDollars(result.tipCents))
        assertEquals("$50.15", Money.formatDollars(result.totalCents))
        assertEquals("$25.08", Money.formatDollars(result.perPersonCents))
    }
}
