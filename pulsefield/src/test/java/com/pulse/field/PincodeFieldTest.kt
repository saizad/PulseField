package com.pulse.field

import com.pulse.field.exts.ValidationError
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PincodeFieldTest : BaseFormFieldTest<String>() {
    override fun createFormField(
        id: String,
        isMandatory: Boolean,
        initialValue: String?
    ): FormField<String> = PincodeField(id, ogField = initialValue, isMandatory = isMandatory)

    override fun createTestValue(): String = "123456"

    override fun createModifiedTestValue(): String = "654321"

    @Test
    fun pincode_validation_fails_for_incorrect_length() = runTest {
        val pincodeField = createFormField(isMandatory = true)

        // Test pincode with incorrect length
        pincodeField.updateField("12345")

        val validationState = pincodeField.validationState.filterError.first()

        assertTrue(
            validationState.errors.exceptions.any {
                it.validationError == ValidationError.INVALID_PINCODE_FORMAT
            }
        )
    }

    @Test
    fun pincode_validation_succeeds_for_correct_length() = runTest {
        val pincodeField = createFormField(isMandatory = true)

        // Test pincode with correct length
        pincodeField.updateField("123456")

        val validationState = pincodeField.validationState.filterSuccess.first()
        assertEquals(validationState, ValidationState.Success)
    }

    @Test
    fun pincode_validation_handles_null_input_for_mandatory_field() = runTest {
        val pincodeField = createFormField(isMandatory = true)

        // Test null input
        pincodeField.updateField(null)

        val validationState = pincodeField.validationState.filterError.first()
        assertTrue(
            validationState.errors.exceptions.any {
                it.validationError == ValidationError.MANDATORY_FIELD
            }
        )
    }

    @Test
    fun pincode_validation_allows_non_mandatory_field_with_null_input() = runTest {
        val pincodeField = createFormField(isMandatory = false)

        // Test null input for non-mandatory field
        pincodeField.updateField(null)

        val validationState = pincodeField.validationState.filterSuccess.first()
        assertEquals(validationState, ValidationState.Success)
    }
}