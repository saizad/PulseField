package com.pulse.field

import com.pulse.field.exts.ValidationException
import com.pulse.field.exts.validateAadhaar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

/**
 * Field class for handling Aadhaar number (Indian national identity number) validation and formatting.
 */
class AadhaarField(
    fieldId: String,
    ogField: String? = null,
    isMandatory: Boolean = false
) : StringField(id = fieldId, isMandatory = isMandatory, ogField = ogField) {

    /**
     * Returns a masked version of the Aadhaar number showing only last 4 digits
     */
    fun maskAadhaar(): Flow<String?> = fieldFlow.map { aadhaarNumber ->
        aadhaarNumber?.let {
            "XXXX XXXX ${stripNonDigits(it).takeLast(4)}"
        }
    }

    override fun validate(value: String?): Flow<List<FieldValidationException>> {
        return super.validate(value)
            .flatMapLatest { validationResults ->
                if (value != null) {
                    val validationErrors = validationResults.toMutableList()
                    try {
                        validateAadhaar(value)
                    } catch (e: ValidationException) {
                        validationErrors.add(FieldValidationException(e.validationError))
                    }
                    flowOf(validationErrors)
                } else {
                    flowOf(validationResults)
                }
            }
    }

    private fun stripNonDigits(field: String): String = 
        field.replace(Regex("[^0-9]"), "")

    fun strip0Aadhaar(field: String?): String = 
        field?.let { stripNonDigits(it).trimStart('0') } ?: ""

    fun formatAadhaar(field: String): String {
        val cleaned = stripNonDigits(field)
        return if (cleaned.length >= 12) {
            "${cleaned.substring(0,4)} ${cleaned.substring(4,8)} ${cleaned.substring(8,12)}"
        } else {
            cleaned
        }
    }
}