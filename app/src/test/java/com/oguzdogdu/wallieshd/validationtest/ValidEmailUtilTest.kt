package com.oguzdogdu.wallieshd.validationtest

import com.oguzdogdu.wallieshd.util.FieldValidators.isValidEmail
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ValidEmailUtilTest {

    @Test
    fun testValidEmail() {
        val validEmail = "test@example.com"
        val isValid = isValidEmail(validEmail)
        assertEquals(true, isValid)
    }

    @Test
    fun testInvalidEmail() {
        val invalidEmail = "invalid-email"
        val isValid = isValidEmail(invalidEmail)
        assertEquals(false, isValid)
    }

    @Test
    fun testEmptyEmail() {
        val emptyEmail = ""
        val isValid = isValidEmail(emptyEmail)
        assertEquals(false, isValid)
    }

    @Test
    fun testValidEmailWithSpecialCharacters() {
        val validEmail = "user.name+1234@example.co.uk"
        val isValid = isValidEmail(validEmail)
        assertEquals(false, isValid)
    }

    @Test
    fun testValidButLongEmail() {
        val validEmail = "thisisaverylongemailaddress@example.com"
        val isValid = isValidEmail(validEmail)
        assertEquals(true, isValid)
    }
}
