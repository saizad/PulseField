package com.pulse.field

import com.pulse.field.exts.ValidationException
import com.pulse.field.exts.validatePhoneNumber
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class PhoneNumberField(
    fieldId: String,
    isMandatory: Boolean = false,
    ogField: String? = null,
) : StringField(id = fieldId, isMandatory = isMandatory, ogField = ogField) {

    override fun validate(value: String?): Flow<List<FieldValidationException>> {
        return super.validate(value)
            .flatMapLatest { exceptions ->
                flow {
                    val validationErrors = exceptions.toMutableList()

                    try {
                        validatePhoneNumber(value)
                    } catch (e: ValidationException) {
                        validationErrors.add(FieldValidationException(e.validationError))
                    }

                    emit(validationErrors)
                }
            }
    }



    fun strip0MobileNumber(field: String): String = field.trimStart('0')
}