package com.pulse.field.ext

import com.pulse.field.exts.ValidationError
import com.pulse.field.exts.ValidationException
import com.pulse.field.exts.validateName
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class NameValidationTest {

    @Test
    fun valid_name() {
        validateName("John")
    }

    @Test
    fun valid_name_with_hyphen() {
        validateName("John-Doe")
    }

    @Test
    fun invalid_name_with_invalid_characters() {
        val exception = assertThrows(ValidationException::class.java) {
            validateName("John123")
        }
        assertEquals(ValidationError.NAME_INVALID_CHARACTERS, exception.validationError)
    }

    @Test
    fun invalid_name_with_too_short_length() {
        val exception = assertThrows(ValidationException::class.java) {
            validateName("J")
        }
        assertEquals(ValidationError.NAME_INVALID_LENGTH, exception.validationError)
    }

    @Test
    fun invalid_name_with_too_long_length() {
        val longName = "John".repeat(20)
        val exception = assertThrows(ValidationException::class.java) {
            validateName(longName)
        }
        assertEquals(ValidationError.NAME_INVALID_LENGTH, exception.validationError)
    }

    @Test
    fun blank_name() {
        val exception = assertThrows(ValidationException::class.java) {
            validateName("")
        }
        assertEquals(ValidationError.NAME_NULL_OR_EMPTY, exception.validationError)
    }
}