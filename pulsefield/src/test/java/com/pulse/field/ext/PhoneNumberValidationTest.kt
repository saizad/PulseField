package com.pulse.field.ext

import com.pulse.field.exts.ValidationError
import com.pulse.field.exts.ValidationException
import com.pulse.field.exts.validatePhoneNumber
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class PhoneNumberValidationTest {

    @Test
    fun valid_phone_number() {
        validatePhoneNumber("1234567890")
    }

    @Test
    fun invalid_phone_number_with_too_few_digits() {
        val exception = assertThrows(ValidationException::class.java) {
            validatePhoneNumber("123456789")
        }
        assertEquals(ValidationError.PHONE_NUMBER_LESS_THAN_10_DIGITS, exception.validationError)
    }

    @Test
    fun invalid_phone_number_with_too_many_digits() {
        val exception = assertThrows(ValidationException::class.java) {
            validatePhoneNumber("12345678901")
        }
        assertEquals(ValidationError.PHONE_NUMBER_MORE_THAN_10_DIGITS, exception.validationError)
    }

    @Test
    fun invalid_phone_number_with_letters() {
        val exception = assertThrows(ValidationException::class.java) {
            validatePhoneNumber("123-ABC-7890")
        }
        assertEquals(ValidationError.INVALID_PHONE_NUMBER_FORMAT, exception.validationError)
    }

    @Test
    fun null_phone_number() {
        val exception = assertThrows(ValidationException::class.java) {
            validatePhoneNumber(null)
        }
        assertEquals(ValidationError.PHONE_NUMBER_NULL_OR_EMPTY, exception.validationError)
    }

    @Test
    fun blank_phone_number() {
        val exception = assertThrows(ValidationException::class.java) {
            validatePhoneNumber("")
        }
        assertEquals(ValidationError.PHONE_NUMBER_NULL_OR_EMPTY, exception.validationError)
    }
}