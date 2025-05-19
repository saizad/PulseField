package com.pulse.field

import com.pulse.field.exts.ValidationError
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AadhaarFieldTest : BaseFormFieldTest<String>() {
    override fun createFormField(
        id: String,
        isMandatory: Boolean,
        initialValue: String?
    ): FormField<String> = AadhaarField(id, isMandatory = isMandatory, ogField = initialValue)

    override fun createTestValue(): String = "1234 5678 9012"

    override fun createModifiedTestValue(): String = "9876 5432 1098"

    @Test
    fun aadhaar_validation_fails_for_null_or_empty_input() = runTest {
        val aadhaarField = createFormField(isMandatory = true)

        aadhaarField.updateField("")

        val validationState = aadhaarField.validationState.filterError.first()
        assertTrue(
            validationState.errors.exceptions.any {
                it.validationError == ValidationError.MANDATORY_FIELD
            }
        )
    }

    @Test
    fun aadhaar_validation_fails_for_incorrect_length() = runTest {
        val aadhaarField = createFormField(isMandatory = true)

        aadhaarField.updateField("123456789")

        val validationState = aadhaarField.validationState.filterError.first()

        assertTrue(
            validationState.errors.exceptions.any {
                it.validationError == ValidationError.INVALID_AADHAR_NUMBER
            }
        )

    }

    @Test
    fun aadhaar_validation_fails_for_invalid_format() = runTest {
        val aadhaarField = createFormField(isMandatory = true)

        aadhaarField.updateField("123456789ABC")

        val validationState = aadhaarField.validationState.filterError.first()

        validationState.errors.exceptions.joinToString { it.validationError.toString() }
        assertTrue(
            validationState.errors.exceptions.any {
                it.validationError == ValidationError.INVALID_AADHAR_NUMBER_FORMAT
            }
        )
    }

    @Test
    fun aadhaar_validation_succeeds_for_valid_formats() = runTest {
        val aadhaarField = createFormField(isMandatory = true)

        val validFormats = listOf(
            "1234 5678 9012",
            "1234-5678-9012",
            "123456789012"
        )

        validFormats.forEach { format ->
            aadhaarField.updateField(format)
            val validationState = aadhaarField.validationState.filterSuccess.first()
            assertEquals(validationState, ValidationState.Success)
        }
    }

    @Test
    fun aadhaar_masking_works_correctly() = runTest {
        val aadhaarField = createFormField() as AadhaarField

        aadhaarField.updateField("1234 5678 9012")

        assertEquals("XXXX XXXX 9012", aadhaarField.maskAadhaar().first())
    }

    @Test
    fun strip_leading_zeros_from_aadhaar() = runTest {
        val aadhaarField = createFormField() as AadhaarField

        assertEquals("1234567890", aadhaarField.strip0Aadhaar("0001234567890"))
    }

    @Test
    fun format_aadhaar_number() = runTest {
        val aadhaarField = createFormField()

        assertEquals("1234 5678 9012", (aadhaarField as AadhaarField).formatAadhaar("123456789012"))
    }

    @Test
    fun aadhaar_validation_allows_non_mandatory_field_with_null_input() = runTest {
        val aadhaarField = createFormField(isMandatory = false)

        aadhaarField.updateField(null)

        val validationState = aadhaarField.validationState.filterSuccess.first()
        assertEquals(validationState, ValidationState.Success)
    }
}