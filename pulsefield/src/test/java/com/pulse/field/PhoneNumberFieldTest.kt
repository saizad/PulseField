package com.pulse.field

import com.pulse.field.exts.ValidationError
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PhoneNumberFieldTest : BaseFormFieldTest<String>() {
    override fun createFormField(
        id: String,
        isMandatory: Boolean,
        initialValue: String?
    ): FormField<String> = PhoneNumberField(id, isMandatory, initialValue)

    override fun createTestValue(): String = "1234567890"
    override fun createModifiedTestValue(): String = "9876543210"

    @Test
    fun valid_phone_number_passes_validation() = runTest {
        val formField = createFormField()
        formField.updateField("1234567890")
        
        val validationState = formField.validationState.filterSuccess.first()
        assertEquals(ValidationState.Success, validationState)
    }

    @Test
    fun phone_number_less_than_10_digits_fails_validation() = runTest {
        val formField = createFormField()
        formField.updateField("123456")

        val validationState = formField.validationState.filterError.first()
        assertTrue(
            validationState.errors.exceptions.any {
                it.validationError == ValidationError.PHONE_NUMBER_LESS_THAN_10_DIGITS
            }
        )
    }

    @Test
    fun phone_number_more_than_10_digits_fails_validation() = runTest {
        val formField = createFormField()
        formField.updateField("123456789012")

        val validationState = formField.validationState.filterError.first()
        assertTrue(
            validationState.errors.exceptions.any {
                it.validationError == ValidationError.PHONE_NUMBER_MORE_THAN_10_DIGITS
            }
        )
    }

    @Test
    fun non_numeric_phone_number_fails_validation() = runTest {
        val formField = createFormField()
        formField.updateField("123abc4567")
        
        val validationState = formField.validationState.filterError.first()
        assertTrue(
            validationState.errors.exceptions.any {
                it.validationError == ValidationError.INVALID_PHONE_NUMBER_FORMAT
            }
        )
    }

    @Test
    fun strip_leading_zero_from_mobile_number() {
        val field = PhoneNumberField("test")
        assertEquals("1234567890", field.strip0MobileNumber("01234567890"))
    }
}