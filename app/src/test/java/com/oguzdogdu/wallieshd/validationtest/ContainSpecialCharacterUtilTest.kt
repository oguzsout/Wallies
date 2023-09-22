package com.oguzdogdu.wallieshd.validationtest

import com.oguzdogdu.wallieshd.util.FieldValidators.isStringContainSpecialCharacter
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class ContainSpecialCharacterUtilTest {

    @Test
    fun `text with special characters should return true`() {
        val text = "Hello@World!"
        assertTrue(isStringContainSpecialCharacter(text))
    }

    @Test
    fun `text without special characters should return false`() {
        val text = "HelloWorld123"
        assertFalse(isStringContainSpecialCharacter(text))
    }

    @Test
    fun `text with only special characters should return true`() {
        val text = "@#$%^"
        assertTrue(isStringContainSpecialCharacter(text))
    }

    @Test
    fun `text with mixed characters should return true`() {
        val text = "Hello123@"
        assertTrue(isStringContainSpecialCharacter(text))
    }

    @Test
    fun `empty text should return false`() {
        val text = ""
        assertFalse(isStringContainSpecialCharacter(text))
    }
}
