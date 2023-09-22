package com.oguzdogdu.wallieshd.util

import com.google.android.material.textfield.TextInputLayout
import com.oguzdogdu.wallieshd.R
import java.util.regex.Pattern

object FieldValidators {

    fun isValidEmailCheck(input: String?, layout: TextInputLayout? = null): Boolean {
        return when {
            input?.isEmpty() == true -> {
                layout?.error = layout?.context?.getString(R.string.required_field)
                false
            }

            !isValidEmail(input.orEmpty()) -> {
                layout?.error = layout?.context?.getString(R.string.invalid_email)
                false
            }

            else -> {
                layout?.isErrorEnabled = false
                true
            }
        }
    }

    fun isValidPasswordCheck(input: String?, layout: TextInputLayout? = null): Boolean {
        return when {
            input?.isEmpty() == true -> {
                layout?.error = layout?.context?.getString(R.string.required_field)
                false
            }

            input?.length.orEmpty() < 6 -> {
                layout?.error = layout?.context?.getString(R.string.password_length)
                false
            }

            !isStringContainNumber(input.orEmpty()) -> {
                layout?.error = layout?.context?.getString(R.string.required_at_least_1_digit)
                false
            }

            !isStringLowerAndUpperCase(input.orEmpty()) -> {
                layout?.error =
                    layout?.context?.getString(
                        R.string.password_must_contain_upper_and_lower_case_letters
                    )
                false
            }

            !isStringContainSpecialCharacter(input.orEmpty()) -> {
                layout?.error = layout?.context?.getString(R.string.one_special_character_required)
                false
            }

            else -> {
                layout?.isErrorEnabled = false
                true
            }
        }
    }

    fun isValidEmail(email: String): Boolean {
        val regex = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}".toRegex()
        return regex.matches(email)
    }

    fun isStringContainNumber(text: String): Boolean {
        val pattern = Pattern.compile(".*\\d.*")
        val matcher = pattern.matcher(text)
        return matcher.matches()
    }

    fun isStringLowerAndUpperCase(text: String): Boolean {
        val lowerCasePattern = Pattern.compile(".*[a-z].*")
        val upperCasePattern = Pattern.compile(".*[A-Z].*")
        val lowerCasePatterMatcher = lowerCasePattern.matcher(text)
        val upperCasePatterMatcher = upperCasePattern.matcher(text)
        return if (!lowerCasePatterMatcher.matches()) {
            false
        } else {
            upperCasePatterMatcher.matches()
        }
    }

    fun isStringContainSpecialCharacter(text: String): Boolean {
        val specialCharacterPattern = Pattern.compile("[^a-zA-Z0-9 ]")
        val specialCharacterMatcher = specialCharacterPattern.matcher(text)
        return specialCharacterMatcher.find()
    }
}
