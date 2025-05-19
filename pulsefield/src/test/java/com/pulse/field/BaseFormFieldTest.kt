package com.pulse.field

import com.pulse.field.exts.ValidationError
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test


abstract class BaseFormFieldTest<T> {
    protected abstract fun createFormField(
        id: String = "testField",
        isMandatory: Boolean = false,
        initialValue: T? = null
    ): FormField<T>

    @Test
    fun update_field_changes_field_flow() = runTest {
        val formField = createFormField(initialValue = null)
        val testValue: T = createTestValue()

        formField.updateField(testValue)

        assertEquals(testValue, formField.fieldFlow.first())
    }

    @Test
    fun clear_resets_field_to_null() = runTest {
        val formField = createFormField(initialValue = createTestValue())

        formField.clear()

        assertNull(formField.fieldFlow.first())
    }

    @Test
   open fun reset_returns_to_original_value() = runTest {
        val originalValue: T = createTestValue()
        val formField = createFormField(initialValue = originalValue)

        val newValue: T = createModifiedTestValue()
        formField.updateField(newValue)
        formField.reset()

        assertEquals(originalValue, formField.fieldFlow.first())
    }

    @Test
    fun mandatory_field_validation() = runTest {
        val formField = createFormField(isMandatory = true)

        formField.updateField(null)

        val validationState = formField.validationState.filterError.first()

        assertTrue(validationState.errors.exceptions.any { it.validationError == ValidationError.MANDATORY_FIELD })
    }

    @Test
    fun modified_state_detection() = runTest {
        val originalValue: T = createTestValue()
        val formField = createFormField(initialValue = originalValue)

        assertFalse(formField.isModified.first()) 

        val newValue: T = createModifiedTestValue()
        formField.updateField(newValue) 
        
        assertTrue(formField.isModified.first()) 

        formField.reset() 
        
        assertFalse(formField.isModified.first()) 
    }

    protected abstract fun createTestValue(): T
    protected abstract fun createModifiedTestValue(): T
}