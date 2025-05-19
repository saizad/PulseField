package com.pulse.field

import com.pulse.field.exts.ValidationError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class QuantityField(
    fieldId: String,
    isMandatory: Boolean = false,
    ogField: Int? = null,
    private val minQuantity: Int = 0,
    private val maxQuantity: Int = Int.MAX_VALUE
) : FormField<Int>(fieldId, isMandatory, ogField) {

    override fun validate(value: Int?): Flow<List<FieldValidationException>> {
        return super.validate(value)
            .flatMapLatest {
                val errors = mutableListOf<FieldValidationException>()

                if (value != null) {
                    if (value < minQuantity) {
                        errors.add(FieldValidationException(ValidationError.QUANTITY_BELOW_MIN))
                    }
                    if (value > maxQuantity) {
                        errors.add(FieldValidationException(ValidationError.QUANTITY_ABOVE_MAX))
                    }
                    flowOf(errors)
                } else {
                    flowOf(it)
                }

            }
    }

    fun increment() {
        val currentValue = fieldFlow.value ?: 0
        if (currentValue < maxQuantity) {
            updateField(currentValue + 1)
        }
    }

    fun decrement() {
        val currentValue = fieldFlow.value ?: 0
        if (currentValue > minQuantity) {
            updateField(currentValue - 1)
        }
    }

    fun canIncrement(): Boolean {
        val currentValue = fieldFlow.value ?: 0
        return currentValue < maxQuantity
    }

    fun canDecrement(): Boolean {
        val currentValue = fieldFlow.value ?: 0
        return currentValue > minQuantity
    }

    fun setToMinimum() {
        updateField(minQuantity)
    }

    fun setToMaximum() {
        updateField(maxQuantity)
    }
} 