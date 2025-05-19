package com.pulse.field

import com.pulse.field.exts.ValidationError
import com.pulse.field.exts.isValidEmail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class EmailField(
    fieldId: String,
    isMandatory: Boolean = false,
    ogField: String? = null,
) : StringField(fieldId, isMandatory, ogField) {

    override fun validate(value: String?): Flow<List<FieldValidationException>> {
        return super.validate(value)
            .flatMapLatest {
                if ((value != null || isMandatory) && value?.isValidEmail() == false) {
                    flowOf(listOf(listOf(FieldValidationException(ValidationError.INVALID_EMAIL)), it).flatten())
                } else {
                    flowOf(it)
                }
            }
    }
}