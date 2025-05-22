package com.pulse.field.ext

import com.pulse.field.exts.ValidationError
import com.pulse.field.exts.ValidationException
import com.pulse.field.exts.validateAadhaar
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class AadhaarValidationTest {

    @Test
    fun valid_aadhaar_number() {
        validateAadhaar("1234 5678 9012")
    }

    @Test
    fun valid_aadhaar_number_with_hyphens() {
         validateAadhaar("1234-5678-9012")
    }

    @Test
    fun valid_aadhaar_number_without_formatting() {
        validateAadhaar("123456789012")
    }

    @Test(expected = ValidationException::class)
    fun invalid_short_aadhar_field() {
        validateAadhaar("12345678")
    }

    @Test
    fun invalid_aadhaar_number_with_letters() {
        val exception = assertThrows(ValidationException::class.java) {
            validateAadhaar("1234-5678-90AB")
        }
        assertEquals(ValidationError.INVALID_AADHAR_NUMBER_FORMAT, exception.validationError)
    }

    @Test
    fun invalid_aadhaar_number_with_too_few_digits() {
        val exception = assertThrows(ValidationException::class.java) {
            validateAadhaar("1234-5678-901")
        }
        assertEquals(ValidationError.INVALID_AADHAR_NUMBER, exception.validationError)
    }

    @Test
    fun invalid_aadhaar_number_with_too_many_digits() {
        val exception = assertThrows(ValidationException::class.java) {
            validateAadhaar("1234-5678-90123")
        }
        assertEquals(ValidationError.INVALID_AADHAR_NUMBER, exception.validationError)
    }

    @Test
    fun blank_aadhaar_number() {
        val exception = assertThrows(ValidationException::class.java) {
            validateAadhaar("")
        }
        assertEquals(ValidationError.AADHAR_NUMBER_NULL_OR_EMPTY, exception.validationError)
    }
}