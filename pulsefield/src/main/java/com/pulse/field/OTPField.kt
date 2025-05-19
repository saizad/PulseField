package com.pulse.field

import com.pulse.field.exts.ValidationError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class OTPField(
    fieldId: String,
    isMandatory: Boolean = true,
    private val count: Int = 4,
    ogField: String? = null
) :
    StringField(fieldId, isMandatory, ogField) {

    override fun validate(value: String?): Flow<List<FieldValidationException>> {
        return super.validate(value)
            .flatMapLatest {
                if (value != null && value.toString().length != count) {
                    flowOf(
                        listOf(
                            listOf(FieldValidationException(ValidationError.INVALID_OTP_FORMAT)),
                            it
                        ).flatten()
                    )
                } else {
                    flowOf(it)
                }
            }
    }
}