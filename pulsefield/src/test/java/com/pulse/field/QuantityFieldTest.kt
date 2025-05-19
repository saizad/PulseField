package com.pulse.field

import com.pulse.field.exts.ValidationError
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class QuantityFieldTest : BaseFormFieldTest<Int>() {

    override fun createFormField(
        id: String,
        isMandatory: Boolean,
        initialValue: Int?
    ): FormField<Int> = QuantityField(id, isMandatory, initialValue)

    override fun createTestValue(): Int = 5
    override fun createModifiedTestValue(): Int = 10

    @Test
    fun test_quantity_below_minimum() = runTest {
        val quantityField = createFormField(initialValue = 0)
        quantityField.updateField(-1)

        val validationState = quantityField.validationState.filterError.first()
        assertTrue(
            validationState.errors.exceptions.any {
                it.validationError == ValidationError.QUANTITY_BELOW_MIN
            }
        )
    }

    @Test
    fun test_quantity_above_maximum() = runTest {
        val quantityField = QuantityField("testField", false, 0, 0, 10)
        quantityField.updateField(11)

        val validationState = quantityField.validationState.filterError.first()
        assertTrue(
            validationState.errors.exceptions.any {
                it.validationError == ValidationError.QUANTITY_ABOVE_MAX
            }
        )
    }

    @Test
    fun test_increment_within_limits() = runTest {
        val quantityField = QuantityField("testField", false, 5, 0, 10)
        quantityField.increment()

        assertEquals(6, quantityField.fieldFlow.first())
    }

    @Test
    fun test_decrement_within_limits() = runTest {
        val quantityField = QuantityField("testField", false, 5, 0, 10)
        quantityField.decrement()

        assertEquals(4, quantityField.fieldFlow.first())
    }

    @Test
    fun test_increment_beyond_maximum() = runTest {
        val quantityField = QuantityField("testField", false, 10, 0, 10)
        quantityField.increment()

        assertEquals(10, quantityField.fieldFlow.first())
    }

    @Test
    fun test_decrement_below_minimum() = runTest {
        val quantityField = QuantityField("testField", false, 0, 0, 10)
        quantityField.decrement()

        assertEquals(0, quantityField.fieldFlow.first())
    }

    @Test
    fun test_can_increment() = runTest {
        val quantityField = QuantityField("testField", false, 9, 0, 10)
        assertTrue(quantityField.canIncrement())

        quantityField.updateField(10)
        assertFalse(quantityField.canIncrement())
    }

    @Test
    fun test_can_decrement() = runTest {
        val quantityField = QuantityField("testField", false, 1, 0, 10)
        assertTrue(quantityField.canDecrement())

        quantityField.updateField(0)
        assertFalse(quantityField.canDecrement())
    }

    @Test
    fun test_set_to_minimum() = runTest {
        val quantityField = QuantityField("testField", false, 5, 0, 10)
        quantityField.setToMinimum()

        assertEquals(0, quantityField.fieldFlow.first())
    }

    @Test
    fun test_set_to_maximum() = runTest {
        val quantityField = QuantityField("testField", false, 5, 0, 10)
        quantityField.setToMaximum()

        assertEquals(10, quantityField.fieldFlow.first())
    }
} 