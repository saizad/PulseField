package com.pulse.field

import com.pulse.field.exts.ValidationError
import com.pulse.field.exts.ValidationException


class CompositeValidationException(
    val fieldId: String,
    val exceptions: List<FieldValidationException>,
    val consolidatedErrorMessage: String = "${exceptions.size} exceptions occurred in $fieldId."
) :
    Exception(consolidatedErrorMessage) {

    val errorMessage by lazy {
        exceptions.map { it.message }.filterNotNull().joinToString { fieldId.plus(":").plus(it) }
    }
}


class FieldValidationException(validationError: ValidationError, val description: String? = null) :
    ValidationException(validationError)