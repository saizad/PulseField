package com.pulse.field.ext

import com.pulse.field.exts.isPhoneValid
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PhoneValidationTest {

    @Test
    fun valid_phone_number_with_parentheses_and_hyphens() {
        assertTrue(isPhoneValid("(123) 456-7890"))
    }

    @Test
    fun valid_phone_number_with_hyphens() {
        assertTrue(isPhoneValid("123-456-7890"))
    }

    @Test
    fun valid_phone_number_with_spaces() {
        assertTrue(isPhoneValid("123 456 7890"))
    }

    @Test
    fun valid_phone_number_without_formatting() {
        assertTrue(isPhoneValid("1234567890"))
    }

    @Test
    fun invalid_phone_number_with_letters() {
        assertFalse(isPhoneValid("123-ABC-7890"))
    }

    @Test
    fun invalid_phone_number_with_too_few_digits() {
        assertFalse(isPhoneValid("123456789"))
    }

    @Test
    fun invalid_phone_number_with_too_many_digits() {
        assertFalse(isPhoneValid("12345678901"))
    }

    @Test
    fun simple_patterns() {
        assertFalse(isPhoneValid("1"))
        assertFalse(isPhoneValid("12"))
        assertFalse(isPhoneValid("123"))
        assertFalse(isPhoneValid("1234"))
        assertFalse(isPhoneValid("12345"))
        assertFalse(isPhoneValid("123456"))
        assertFalse(isPhoneValid("1234567"))
        assertFalse(isPhoneValid("12345678"))
        assertFalse(isPhoneValid("123456789"))
        assertTrue(isPhoneValid("1234567890"))
    }

    @Test
    fun null_phone_number() {
        assertFalse(isPhoneValid(null))
    }

    @Test
    fun blank_phone_number() {
        assertFalse(isPhoneValid(""))
    }
}