package com.pulse.field

import com.pulse.field.exts.ValidationError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

open class ConfirmPasswordField(
    id: String = "confirm_password",
    isMandatory: Boolean = true,
    ogField: String? = null,
    val passwordField: PasswordField,
) : PasswordField(id = id, isMandatory = isMandatory, ogField = ogField) {


    override fun validate(value: String?): Flow<List<FieldValidationException>> {
        return combine(
            super.validate(value),
            passwordField.fieldFlow
        ) { existingErrors, originalPassword ->
            val errors = existingErrors.toMutableList()

            if (value != originalPassword) {
                errors.add(FieldValidationException(ValidationError.PASSWORD_NO_MATCH))
            }
            errors
        }
    }
}