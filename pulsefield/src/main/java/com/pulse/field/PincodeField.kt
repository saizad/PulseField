package com.pulse.field

import com.pulse.field.exts.ValidationError
import com.pulse.field.exts.isValidIndianPincode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class PincodeField(
    fieldId: String,
    ogField: String? = null,
    isMandatory: Boolean = false,
) : FormField<String>(id = fieldId, isMandatory = isMandatory, ogField = ogField) {


    override fun validate(value: String?): Flow<List<FieldValidationException>> {
        return super.validate(value)
            .flatMapLatest {
                if (value != null && !isValidIndianPincode(value)) {
                    flowOf(
                        listOf(
                            listOf(FieldValidationException(ValidationError.INVALID_PINCODE_FORMAT)),
                            it
                        ).flatten()
                    )
                } else {
                    flowOf(it)
                }
            }
    }

}