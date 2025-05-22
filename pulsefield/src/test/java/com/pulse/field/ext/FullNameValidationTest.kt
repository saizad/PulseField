package com.pulse.field.ext

import com.pulse.field.exts.ValidationError
import com.pulse.field.exts.ValidationException
import com.pulse.field.exts.validateFullName
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class FullNameValidationTest {

    @Test
    fun valid_full_name() {
        validateFullName("John Doe")
    }

    @Test(expected =  ValidationException::class)
    fun valid_full_name_with_hyphen() {
        validateFullName("John-Doe")
    }

    @Test
    fun invalid_full_name_with_single_name() {
        val exception = assertThrows(ValidationException::class.java) {
            validateFullName("John")
        }
        assertEquals(ValidationError.FULL_NAME_LAST_NAME_MISSING, exception.validationError)
    }

    @Test
    fun invalid_full_name_with_invalid_characters() {
        val exception = assertThrows(ValidationException::class.java) {
            validateFullName("John123 Doe")
        }
        assertEquals(ValidationError.FULL_NAME_INVALID_CHARACTERS, exception.validationError)
    }

    @Test
    fun single_chars_firstname_and_lastname() {
        validateFullName("J D")
    }

    @Test
    fun invalid_full_name_with_too_long_length() {
        val longName = "John".repeat(30)
        val exception = assertThrows(ValidationException::class.java) {
            validateFullName(longName)
        }
        assertEquals(ValidationError.FULL_NAME_INVALID_LENGTH, exception.validationError)
    }

    @Test
    fun blank_full_name() {
        val exception = assertThrows(ValidationException::class.java) {
            validateFullName("")
        }
        assertEquals(ValidationError.FULL_NAME_NULL_OR_EMPTY, exception.validationError)
    }
}