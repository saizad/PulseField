package com.pulse.field

import com.pulse.field.exts.ValidationError
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.Timeout

class ConfirmPasswordFieldTest : BaseFormFieldTest<String>() {

    private val passwordField = PasswordField("password", true, "Password123")

    @Rule
    @JvmField
    val globalTimeout: Timeout = Timeout.seconds(3)


    override fun createFormField(
        id: String,
        isMandatory: Boolean,
        initialValue: String?
    ): FormField<String> = ConfirmPasswordField(
        id,
        passwordField = passwordField,
        isMandatory = isMandatory,
        ogField = initialValue
    )

    override fun createTestValue(): String = "Password123"

    override fun createModifiedTestValue(): String = "NewPassword456"

    @Test
    fun confirm_password_validation_fails_when_passwords_dont_match() = runTest {
        val confirmPasswordField = createFormField(isMandatory = true)

        // Set a different password than the original
        confirmPasswordField.updateField("DifferentPassword")

        val validationState = confirmPasswordField.validationState.filterError.first()

        assertTrue(
            validationState.errors.exceptions.any {
                it.validationError == ValidationError.PASSWORD_NO_MATCH
            }
        )
    }

    @Test
    fun confirm_password_validation_succeeds_when_passwords_match() = runTest {
        val confirmPasswordField = createFormField(isMandatory = true)

        // Set the same password as the original
        confirmPasswordField.updateField("Password123")

        val validationState = confirmPasswordField.validationState.filterSuccess.first()
        assertEquals(validationState, ValidationState.Success)
    }

//    @Test
//    fun confirm_password_validation_updates_when_original_password_changes() = runTest {
//        val confirmPasswordField = createFormField(isMandatory = true) as ConfirmPasswordField
//
//        // Initially set matching passwords
//        confirmPasswordField.updateField("Password123")
//
//        // Validation should succeed
//        val initialValidation = confirmPasswordField.validationState.filterSuccess.first()
//        assertEquals(initialValidation, ValidationState.Success)
//
//        // Change the original password
//        passwordField.updateField("NewPassword456")
//
//        // Validation should now fail
//        val updatedValidation = confirmPasswordField.validationState.filterError.first()
//        assertTrue(
//            updatedValidation.errors.exceptions.any {
//                it.validationError == ValidationError.PASSWORD_NO_MATCH
//            }
//        )
//
//        // Update confirm password to match again
//        confirmPasswordField.updateField("NewPassword456")
//
//        // Validation should succeed again
//        val finalValidation = confirmPasswordField.validationState.filterSuccess.first()
//        assertEquals(finalValidation, ValidationState.Success)
//    }

    @Test
    fun confirm_password_validation_handles_null_input_for_mandatory_field() = runTest {
        val confirmPasswordField = createFormField(isMandatory = true)

        // Test null input
        confirmPasswordField.updateField(null)

        val validationState = confirmPasswordField.validationState.filterError.first()
        assertTrue(
            validationState.errors.exceptions.any {
                it.validationError == ValidationError.MANDATORY_FIELD
            }
        )
    }

    @Test
    fun confirm_password_validation_handles_empty_input_for_mandatory_field() = runTest {
        val confirmPasswordField = createFormField(isMandatory = true)

        // Test empty input
        confirmPasswordField.updateField("")

        val validationState = confirmPasswordField.validationState.filterError.first()
        assertTrue(
            validationState.errors.exceptions.any {
                it.validationError == ValidationError.MANDATORY_FIELD
            }
        )
    }

    @Test
    fun confirm_password_validation_checks_both_mandatory_and_matching() = runTest {
        val confirmPasswordField = createFormField(isMandatory = true)

        // Set a different password than the original
        confirmPasswordField.updateField("DifferentPassword")

        val validationState = confirmPasswordField.validationState.filterError.first()

        // Should only have the PASSWORD_NO_MATCH error, not MANDATORY_FIELD
        assertEquals(1, validationState.errors.exceptions.size)
        assertEquals(
            ValidationError.PASSWORD_NO_MATCH,
            validationState.errors.exceptions[0].validationError
        )
    }
}