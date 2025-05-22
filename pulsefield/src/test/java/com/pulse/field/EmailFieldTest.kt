package com.pulse.field

import com.pulse.field.exts.ValidationError
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

class EmailFieldTest : BaseFormFieldTest<String>() {
    override fun createFormField(
        id: String,
        isMandatory: Boolean,
        initialValue: String?
    ): FormField<String> = EmailField(id, isMandatory, initialValue)

    override fun createTestValue(): String = "test@example.com"
    override fun createModifiedTestValue(): String = "different@example.com"

    @Test
    fun test_loading_state_is_emitted_first() = runTest {
        val emailField = createFormField()
        emailField.updateField("test@example.com")

        // Verify the first emission is Loading
        val loadingState = emailField.validationState.filterLoading.first()
        assertEquals(loadingState, ValidationState.Loading)
    }

    @Test
    fun test_mandatory_field_with_null_value() = runTest {
        val emailField = createFormField(isMandatory = true)
        emailField.updateField(null)

        // Verify the error state
        val errorState = emailField.validationState.filterError.first()
        val errors = errorState.errors.exceptions
        assertEquals(1, errors.size)
        assertEquals(ValidationError.MANDATORY_FIELD, errors[0].validationError)
    }

    @Test
    fun test_mandatory_field_with_empty_value() = runTest {
        val emailField = createFormField(isMandatory = true)
        emailField.updateField("")

        // Verify the error state
        val errorState = emailField.validationState.filterError.first()
        val errors = errorState.errors.exceptions
        assertEquals(1, errors.size)
        assertEquals(ValidationError.MANDATORY_FIELD, errors[0].validationError)
    }

    @Test
    fun test_non_mandatory_field_with_null_value() = runTest {
        val emailField = createFormField()
        emailField.updateField(null)

        // Verify the success state
        val successState = emailField.validationState.filterSuccess.first()
        assertEquals(successState, ValidationState.Success)
    }

    @Test
    fun test_valid_email_format() = runTest {
        val emailField = createFormField(isMandatory = true)
        emailField.updateField("test@example.com")

        // Verify the success state
        val successState = emailField.validationState.filterSuccess.first()
        assertEquals(successState, ValidationState.Success)
    }

    @Test
    fun test_invalid_email_format() = runTest {
        val emailField = createFormField(isMandatory = true)
        emailField.updateField("invalid-email")

        // Verify the error state
        val errorState = emailField.validationState.filterError.first()
        val errors = errorState.errors.exceptions
        assertEquals(1, errors.size)
        assertEquals(ValidationError.INVALID_EMAIL, errors[0].validationError)
    }

    @Test
    fun test_field_modification() = runTest {
        val originalValue = "original@example.com"
        val emailField = createFormField(isMandatory = true, initialValue = originalValue)

        // Initially, the field should not be modified
        assertFalse(emailField.isModified.first())

        // Update the field with a new value
        emailField.updateField("new@example.com")
        assertTrue(emailField.isModified.first())

        // Reset the field to the original value
        emailField.reset()
        assertFalse(emailField.isModified.first())
    }

    @Test
    fun test_field_clearing() = runTest {
        val originalValue = "original@example.com"
        val emailField = createFormField(isMandatory = true, initialValue = originalValue)

        // Clear the field
        emailField.clear()
        assertNull(emailField.fieldFlow.value)
    }

    @Test
    fun test_multiple_updates_to_field() = runTest {
        val emailField = createFormField(isMandatory = true)

        // First update: Invalid email
        emailField.updateField("invalid-email")
        val errorState = emailField.validationState.filterError.first()
        assertEquals(ValidationError.INVALID_EMAIL, errorState.errors.exceptions[0].validationError)

        // Second update: Valid email
        emailField.updateField("test@example.com")
        val successState = emailField.validationState.filterSuccess.first()
        assertEquals(successState, ValidationState.Success)
    }

    @Test
    fun test_empty_string_after_non_empty_value() = runTest {
        val emailField = createFormField(isMandatory = true)

        // First update: Valid email
        emailField.updateField("test@example.com")
        val successState = emailField.validationState.filterSuccess.first()
        assertEquals(successState, ValidationState.Success)

        // Second update: Empty string
        emailField.updateField("")
        val errorState = emailField.validationState.filterError.first()
        assertEquals(ValidationError.MANDATORY_FIELD, errorState.errors.exceptions[0].validationError)
    }
}