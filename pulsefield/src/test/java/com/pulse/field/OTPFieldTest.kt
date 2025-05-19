package com.pulse.field

import com.pulse.field.exts.ValidationError
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OTPFieldTest : BaseFormFieldTest<String>() {
    override fun createFormField(
        id: String,
        isMandatory: Boolean,
        initialValue: String?
    ): FormField<String> {
        return OTPField(id, count = 4, isMandatory = isMandatory, ogField = initialValue)
    }

    override fun createTestValue(): String = "1234"

    override fun createModifiedTestValue(): String = "5678"

    @Test
    fun otp_validation_fails_for_incorrect_length() = runTest {
        val otpField = createFormField(isMandatory = true)

        // Test OTP with incorrect length
        otpField.updateField("12")

        val validationState = otpField.validationState.filterError.first()
        assertTrue(
            validationState.errors.exceptions.any {
                it.validationError == ValidationError.INVALID_OTP_FORMAT
            }
        )
    }

    @Test
    fun otp_validation_succeeds_for_correct_length() = runTest {
        val otpField = createFormField(isMandatory = true)

        // Test OTP with correct length
        otpField.updateField("1234")

        val validationState = otpField.validationState.filterSuccess.first()
        assertEquals(validationState, ValidationState.Success)
    }

    @Test
    fun otp_validation_handles_null_input_for_mandatory_field() = runTest {
        val otpField = createFormField(isMandatory = true)

        // Test null input
        otpField.updateField(null)

        val validationState = otpField.validationState.filterError.first()
        assertTrue(
            validationState.errors.exceptions.any {
                it.validationError == ValidationError.MANDATORY_FIELD
            }
        )
    }

    @Test
    fun otp_validation_allows_non_mandatory_field_with_null_input() = runTest {
        val otpField = createFormField(isMandatory = false)

        // Test null input for non-mandatory field
        otpField.updateField(null)

        val validationState = otpField.validationState.filterSuccess.first()
        assertEquals(validationState, ValidationState.Success)
    }
}