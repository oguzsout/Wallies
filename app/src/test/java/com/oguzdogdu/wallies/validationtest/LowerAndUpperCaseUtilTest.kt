package com.oguzdogdu.wallies.validationtest

import com.oguzdogdu.wallies.util.FieldValidators.isStringLowerAndUpperCase
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class LowerAndUpperCaseUtilTest {

    @Test
    fun `text with both lower and upper case should return true`() {
        val text = "HelloWorld"
        assertTrue(isStringLowerAndUpperCase(text))
    }

    @Test
    fun `text with only lowercase should return false`() {
        val text = "helloworld"
        assertFalse(isStringLowerAndUpperCase(text))
    }

    @Test
    fun `text with only uppercase should return false`() {
        val text = "HELLOWORLD"
        assertFalse(isStringLowerAndUpperCase(text))
    }

    @Test
    fun `text with special characters should return false`() {
        val text = "@#$%^"
        assertFalse(isStringLowerAndUpperCase(text))
    }

    @Test
    fun `text with mixed characters should return false`() {
        val text = "Hello123"
        assertFalse(isStringLowerAndUpperCase(text))
    }
}
