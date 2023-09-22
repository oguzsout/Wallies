package com.oguzdogdu.wallieshd.validationtest

import com.oguzdogdu.wallieshd.util.FieldValidators.isStringContainNumber
import junit.framework.TestCase.assertEquals
import org.junit.Test

class StringContainerUtilTest {
    @Test
    fun testStringContainsNumber() {
        val text = "This is a text with 123 numbers"
        val containsNumber = isStringContainNumber(text)
        assertEquals(true, containsNumber)
    }

    @Test
    fun testStringWithoutNumber() {
        val text = "This is a text without numbers"
        val containsNumber = isStringContainNumber(text)
        assertEquals(false, containsNumber)
    }

    @Test
    fun testEmptyString() {
        val text = ""
        val containsNumber = isStringContainNumber(text)
        assertEquals(false, containsNumber)
    }

    @Test
    fun testSpecialCharacters() {
        val text = "!@#$%^&*()_+"
        val containsNumber = isStringContainNumber(text)
        assertEquals(false, containsNumber)
    }
}
