package com.pulse.field

import com.pulse.field.exts.ValidationError
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GenderFieldTest : BaseFormFieldTest<Gender>() {
    override fun createFormField(
        id: String,
        isMandatory: Boolean,
        initialValue: Gender?
    ): FormField<Gender> = GenderField(id, isMandatory = isMandatory, ogField = initialValue)

    override fun createTestValue(): Gender = Gender.MALE

    override fun createModifiedTestValue(): Gender = Gender.FEMALE

    @Test
    fun gender_validation_fails_for_mandatory_field_with_unspecified() = runTest {
        val genderField = createFormField(isMandatory = true)

        genderField.updateField(Gender.UNSPECIFIED)

        val validationState = genderField.validationState.filterError.first()

        assertTrue(
            validationState.errors.exceptions.any {
                it.validationError == ValidationError.INVALID_GENDER
            }
        )
    }

    @Test
    fun gender_validation_succeeds_for_non_unspecified_genders() = runTest {
        val genderField = createFormField(isMandatory = true)

        // Test for each non-UNSPECIFIED gender
        listOf(Gender.MALE, Gender.FEMALE, Gender.OTHER).forEach { gender ->
            genderField.updateField(gender)

            val validationState = genderField.validationState.filterSuccess.first()
            assertEquals(validationState, ValidationState.Success)
        }
    }

    @Test
    fun gender_validation_handles_null_input_for_mandatory_field() = runTest {
        val genderField = createFormField(isMandatory = true)

        genderField.updateField(null)

        val validationState = genderField.validationState.filterError.first()

        assertTrue(
            validationState.errors.exceptions.any {
                it.validationError == ValidationError.MANDATORY_FIELD
            }
        )
    }

    @Test
    fun gender_validation_allows_non_mandatory_field_with_unspecified() = runTest {
        val genderField = createFormField(isMandatory = false)

        genderField.updateField(Gender.UNSPECIFIED)

        val validationState = genderField.validationState.filterSuccess.first()
        assertEquals(validationState, ValidationState.Success)
    }

    @Test
    fun gender_validation_allows_non_mandatory_field_with_null_input() = runTest {
        val genderField = createFormField(isMandatory = false)

        genderField.updateField(null)

        val validationState = genderField.validationState.filterSuccess.first()
        assertEquals(validationState, ValidationState.Success)
    }
}