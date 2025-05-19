package com.pulse.field

import com.pulse.field.exts.ValidationError
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertEquals

class NameFieldTest : BaseFormFieldTest<String>() {
    override fun createFormField(
        id: String, isMandatory: Boolean, initialValue: String?
    ): FormField<String> = NameField(id, isMandatory = isMandatory, ogField = initialValue)

    override fun createTestValue(): String = "John"

    override fun createModifiedTestValue(): String = "Jane"

    @Test
    fun name_validation_fails_for_null_or_empty_input() = runTest {
        val nameField = createFormField(isMandatory = true)

        nameField.updateField("")

        val validationState = nameField.validationState.filterError.first()

        assertTrue(validationState.errors.exceptions.any {
            it.validationError == ValidationError.MANDATORY_FIELD
        })
    }

    @Test
    fun name_validation_fails_for_too_short_name() = runTest {
        val nameField = createFormField(isMandatory = true)

        nameField.updateField("A")

        val validationState = nameField.validationState.filterError.first()

        assertTrue(validationState.errors.exceptions.any {
            it.validationError == ValidationError.NAME_INVALID_LENGTH
        })
    }

    @Test
    fun name_validation_fails_for_too_long_name() = runTest {
        val nameField = createFormField(isMandatory = true)

        nameField.updateField("A".repeat(51))

        val validationState = nameField.validationState.filterError.first()
        assertTrue(validationState.errors.exceptions.any {
            it.validationError == ValidationError.NAME_INVALID_LENGTH
        })
    }

    @Test
    fun name_validation_fails_for_invalid_characters() = runTest {
        val nameField = createFormField(isMandatory = true)

        nameField.updateField("John123")

        val validationState = nameField.validationState.filterError.first()

        assertTrue(validationState.errors.exceptions.any {
            it.validationError == ValidationError.NAME_INVALID_CHARACTERS
        })
    }

    @Test
    fun name_validation_succeeds_for_valid_name() = runTest {
        val nameField = createFormField(isMandatory = true)

        nameField.updateField("John-Doe")

        val validationState = nameField.validationState.filterSuccess.first()
        assertEquals(validationState, ValidationState.Success)
    }

    @Test
    fun name_validation_allows_non_mandatory_field_with_null_input() = runTest {
        val nameField = createFormField(isMandatory = false)

        nameField.updateField(null)

        val validationState = nameField.validationState.filterSuccess.first()
        assertEquals(validationState, ValidationState.Success)
    }
}