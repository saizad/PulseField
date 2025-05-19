package com.pulse.field

import com.pulse.field.exts.ValidationError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

open class ListField<T>(
    id: String,
    isMandatory: Boolean = false,
    ogField: List<T>? = null,
) : FormField<List<T>>(id = id, isMandatory = isMandatory, ogField = ogField) {
    override fun validate(value: List<T>?): Flow<List<FieldValidationException>> {
       return super.validate(value)
            .flatMapLatest {
                val errors = mutableListOf<FieldValidationException>()
                if (value != null) {
                    if (value.isEmpty()) {
                        errors.add(FieldValidationException(ValidationError.EMPTY_LIST))
                    }
                    flowOf(errors)
                } else {
                    flowOf(it)
                }

            }
    }
}