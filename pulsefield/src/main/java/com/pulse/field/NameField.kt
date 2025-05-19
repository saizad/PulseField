package com.pulse.field

import com.pulse.field.exts.ValidationException
import com.pulse.field.exts.validateName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class NameField(
    fieldId: String,
    isMandatory: Boolean = false,
    ogField: String? = null,
) : StringField(id = fieldId, isMandatory = isMandatory, ogField = ogField) {


    override fun validate(value: String?): Flow<List<FieldValidationException>> {
        return super.validate(value)
            .flatMapLatest {
                if (value != null) {
                    val validationErrors = it.toMutableList()
                    try {
                        validateName(value)
                    } catch (e: ValidationException) {
                        validationErrors.add(FieldValidationException(e.validationError))
                    }
                    flowOf(validationErrors)
                } else {
                    flowOf(it)
                }
            }
    }


}
