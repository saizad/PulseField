package com.pulse.field

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

abstract class BaseDataFormTest<T> {
    protected abstract fun createDataForm(
        id: String = "testForm",
    ): DataForm<T>

    protected abstract fun createTestValue(): T
    protected abstract fun createModifiedTestValue(): T
    abstract fun modifyField(field: FormField<*>)
    abstract fun updateWithValidValue(field: FormField<*>)

    @Test
    fun validation_state_combines_field_validations() = runTest {
        val dataForm = createDataForm()

        // Initially should be in loading state
        assertTrue(dataForm.validationState.first() is ValidationState.Loading)

        // When all fields are valid
        dataForm.formFields.forEach { field ->
            updateWithValidValue(field)
        }

        // Should eventually be success
        assertTrue(dataForm.validationState.first() is ValidationState.Success)
    }

    @Test
    fun clear_all_form_fields() = runTest {
        val dataForm = createDataForm()

        dataForm.clear()

        dataForm.formFields.forEach { field ->
            assertNull(field.fieldFlow.first())
        }
    }

    @Test
    fun reset_returns_all_fields_to_original_values() = runTest {
        val dataForm = createDataForm()
        val formFields = dataForm.formFields

        // Modify all fields
        formFields.forEach { field ->
            field.updateField(null)
        }

        dataForm.reset()

        // Verify all fields are reset to original values
        formFields.forEach { field ->
            assertEquals(field.ogField, field.fieldFlow.first())
        }
    }

    @Test
    fun is_modified_tracks_field_modifications() = runTest {
        val dataForm = createDataForm()
        val formFields = dataForm.formFields

        // Initially not modified
        assertFalse(dataForm.isModified.first())

        // Modify a field
        modifyField(formFields.first())

        // Should be marked as modified
        assertTrue(dataForm.isModified.first())

        // Reset should clear modified state
        dataForm.reset()
        assertFalse(dataForm.isModified.first())
    }


    @Test
    fun required_find_field_returns_correct_field() = runTest {
        val dataForm = createDataForm()
        val formFields = dataForm.formFields

        val firstField = formFields.first()
        val foundField = dataForm.requiredFindField<FormField<*>>(firstField.id)

        assertEquals(firstField.id, foundField.id)
    }

    @Test(expected = IllegalStateException::class)
    fun required_find_field_throws_for_invalid_field() = runTest {
        val dataForm = createDataForm()
        dataForm.requiredFindField<FormField<*>>("non_existent_field")
    }

    @Test
    fun update_build_updates_form_value() = runTest {
        val dataForm = createDataForm()

        val testValue = createTestValue()

        // Mock the build method to return test value
        dataForm.updateBuild().first()

        assertEquals(testValue, dataForm.fieldFlow.first())
    }
} 