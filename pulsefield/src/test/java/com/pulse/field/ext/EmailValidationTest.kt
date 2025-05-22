package com.pulse.field.ext

import com.pulse.field.exts.isValidEmail
import com.pulse.field.exts.isValidEmailRegex
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EmailValidationTest {

    @Test
    fun valid_email() {
        assertTrue("test@example.com".isValidEmail())
        assertTrue("test@example.com".isValidEmailRegex())
    }

    @Test
    fun valid_email_with_subdomain() {
        assertTrue("test@sub.example.com".isValidEmail())
        assertTrue("test@sub.example.com".isValidEmailRegex())
    }

    @Test
    fun invalid_email_with_missing__() {
        assertFalse("testexample.com".isValidEmail())
        assertFalse("testexample.com".isValidEmailRegex())
    }

    @Test
    fun invalid_email_with_consecutive_dots() {
        assertFalse("test@example..com".isValidEmail())
        assertFalse("test@example..com".isValidEmailRegex())
    }

    @Test
    fun invalid_email_with_missing_domain() {
        assertFalse("test@.com".isValidEmail())
        assertFalse("test@.com".isValidEmailRegex())
    }

    @Test
    fun invalid_email_with_missing_username() {
        assertFalse("@example.com".isValidEmail())
        assertFalse("@example.com".isValidEmailRegex())
    }

    @Test
    fun invalid_email_with_invalid_characters() {
        assertFalse("test@exa#mple.com".isValidEmail())
        assertFalse("test@exa#mple.com".isValidEmailRegex())
    }
}