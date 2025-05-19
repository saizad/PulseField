package com.pulse.field

import com.pulse.field.exts.ValidationException
import com.pulse.field.exts.validateFullName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FullNameField(
    fieldId: String,
    ogField: String? = null,
    isMandatory: Boolean = false
) : StringField(id = fieldId, isMandatory = isMandatory, ogField = ogField) {

    fun firstName(): Flow<String?> = fieldFlow.map {
        it?.trim()?.split(" ")?.firstOrNull()
    }

    fun lastName(): Flow<String?> = fieldFlow.map {
        it?.trim()?.split(" ")?.drop(1)?.joinToString(" ")
    }

    override fun validate(value: String?): Flow<List<FieldValidationException>> {
        return super.validate(value)
            .flatMapLatest {
                if (value != null) {
                    val validationErrors = it.toMutableList()
                    try {
                        validateFullName(value)
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