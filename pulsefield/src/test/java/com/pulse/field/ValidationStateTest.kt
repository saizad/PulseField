package com.pulse.field

import com.pulse.field.exts.ValidationError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class ValidationStateTest {

    @Test
    fun errors_property_returns_empty_list_for_loading_state() {
        val state = ValidationState.Loading
        assertTrue(state.errors.isEmpty())
    }

    @Test
    fun errors_property_returns_empty_list_for_success_state() {
        val state = ValidationState.Success
        assertTrue(state.errors.isEmpty())
    }

    @Test
    fun errors_property_returns_exceptions_list_for_error_state() {
        val exceptions = listOf(
            FieldValidationException(ValidationError.INVALID_EMAIL, "Custom description 1"),
            FieldValidationException(ValidationError.MANDATORY_FIELD, "Custom description 2")
        )
        val state = ValidationState.Error(CompositeValidationException("email_field", exceptions))
        assertEquals(exceptions, state.errors.exceptions)
    }

    @Test
    fun flow_extensions_filter_correct_validation_state_types() = runTest {
        val states = flowOf(
            ValidationState.Loading,
            ValidationState.Success,
            ValidationState.Error(CompositeValidationException("test_field", emptyList()))
        )

        assertIs<ValidationState.Loading>(states.filterLoading.first())
        assertIs<ValidationState.Success>(states.filterSuccess.first())
        assertIs<ValidationState.Error>(states.filterError.first())
    }

    @Test
    fun error_flow_formats_error_messages_correctly() = runTest {
        val emailField = EmailField(
            fieldId = "email_field",
            isMandatory = true,
            ogField = null
        )
        emailField.updateField("invalid.email")
        val expected = ValidationError.INVALID_EMAIL.description
        assertEquals(expected, emailField.validationState.filterError.errors.first().exceptions.first().message)
    }

    @Test
    fun error_flow_errors_extension_returns_correct_exceptions() = runTest {
        val emailField = EmailField(
            fieldId = "email_field",
            isMandatory = true,
            ogField = null
        )
        emailField.updateField("invalid.email")

        val expectedExceptions = listOf(
            FieldValidationException(ValidationError.INVALID_EMAIL),
        )
        assertEquals(
            expectedExceptions.map { it.validationError },
            emailField.validationState.filterError.first().errors.exceptions
                .map { it.validationError }
        )
    }

    @Test
    fun test_error_property_formats_exceptions_correctly() = runTest {
        val description = "Test Description"
        val formField = object : FormField<Any>("testfield", true, null) {
            override fun validate(value: Any?): Flow<List<FieldValidationException>> {
                return flowOf(listOf(FieldValidationException(ValidationError.MANDATORY_FIELD,
                    description
                )))
            }
        }
        val expectedMessage = "${ValidationError.MANDATORY_FIELD.description}($description)"
        assertEquals(expectedMessage, formField.error.first())
    }
} 