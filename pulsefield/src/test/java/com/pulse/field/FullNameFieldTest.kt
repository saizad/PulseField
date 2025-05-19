package com.pulse.field

import com.pulse.field.exts.ValidationError
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FullNameFieldTest : BaseFormFieldTest<String>() {
    override fun createFormField(
        id: String,
        isMandatory: Boolean,
        initialValue: String?
    ): FormField<String> = FullNameField(id, isMandatory = isMandatory, ogField = initialValue)

    override fun createTestValue(): String = "John Doe"

    override fun createModifiedTestValue(): String = "Jane Smith"

    @Test
    fun full_name_validation_fails_for_null_or_empty_input() = runTest {
        val fullNameField = createFormField(isMandatory = true)

        fullNameField.updateField("")

        val validationState = fullNameField.validationState.filterError.first()

        assertTrue(
            validationState.errors.exceptions.any {
                it.validationError == ValidationError.MANDATORY_FIELD
            }
        )
    }

    @Test
    fun full_name_validation_fails_for_single_name() = runTest {
        val fullNameField = createFormField(isMandatory = true)

        fullNameField.updateField("John")

        val validationState = fullNameField.validationState.filterError.first()

        assertTrue(
            validationState.errors.exceptions.any {
                it.validationError == ValidationError.FULL_NAME_LAST_NAME_MISSING
            }
        )
    }

    @Test
    fun full_name_validation_fails_for_too_long_name() = runTest {
        val fullNameField = createFormField(isMandatory = true)

        fullNameField.updateField("A".repeat(101))

        val validationState = fullNameField.validationState.filterError.first()

        assertTrue(
            validationState.errors.exceptions.any {
                it.validationError == ValidationError.FULL_NAME_INVALID_LENGTH
            }
        )
    }

    @Test
    fun full_name_validation_fails_for_invalid_characters() = runTest {
        val fullNameField = createFormField(isMandatory = true)

        fullNameField.updateField("John123 Doe456")

        val validationState = fullNameField.validationState.filterError.first()

        assertTrue(
            validationState.errors.exceptions.any {
                it.validationError == ValidationError.FULL_NAME_INVALID_CHARACTERS
            }
        )
    }

    @Test
    fun full_name_validation_succeeds_for_valid_name() = runTest {
        val fullNameField = createFormField(isMandatory = true)

        fullNameField.updateField("John Michael-Doe")

        val validationState = fullNameField.validationState.filterSuccess.first()
        assertEquals(validationState, ValidationState.Success)
    }

    @Test
    fun first_name_extraction_works_correctly() = runTest {
        val fullNameField = createFormField() as FullNameField
        fullNameField.updateField("John Michael Doe")

        assertEquals("John", fullNameField.firstName().first())
    }

    @Test
    fun last_name_extraction_works_correctly() = runTest {
        val fullNameField = createFormField() as FullNameField
        fullNameField.updateField("John Michael Doe")

        assertEquals("Michael Doe", fullNameField.lastName().first())
    }

    @Test
    fun full_name_validation_allows_non_mandatory_field_with_null_input() = runTest {
        val fullNameField = createFormField(isMandatory = false)

        fullNameField.updateField(null)

        val validationState = fullNameField.validationState.filterSuccess.first()
        assertEquals(validationState, ValidationState.Success)
    }
}