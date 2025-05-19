package com.pulse.field.exts

import com.pulse.field.FieldValidationException
import java.util.regex.Pattern
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun String.keepNumbersOnly() =
    replace(Regex("[^0-9]"), "")

fun String.keepNumbersAndAlphabets() = replace(Regex("[^A-Za-z0-9]"), "")

fun isPhoneValid(phone: String?): Boolean {
    val pattern =
        "^\\(?([0-9]{3})\\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})\$"
    return if (!phone.isNullOrBlank()) {
        Pattern.matches(pattern, phone)
    } else {
        false
    }
}

fun String.isValidEmail(): Boolean {
    val emailRegex = Regex(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
    )
    return this.matches(emailRegex)
}


fun String.isValidEmailRegex(): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    return matches(emailRegex) && !contains("..")
}

fun validateAadhaar(field: String) {
    // Check for null or blank
    requireValidationError(field.isNotBlank(), ValidationError.AADHAR_NUMBER_NULL_OR_EMPTY)


    // Remove non-numeric characters and verify length
    val cleanedField = field.keepNumbersAndAlphabets()
    requireValidationError(cleanedField.length == 12, ValidationError.INVALID_AADHAR_NUMBER)

    // First validate the format using regex
    requireValidationError(
        field.matches(Regex("^[0-9]{4}[ -]?[0-9]{4}[ -]?[0-9]{4}$")),
        ValidationError.INVALID_AADHAR_NUMBER_FORMAT
    )
}

@Throws(FieldValidationException::class)
fun validateFullName(field: String) {
    requireValidationError(field.isNotBlank(), ValidationError.FULL_NAME_NULL_OR_EMPTY)

    val trimmedField = field.trim()

    requireValidationError(trimmedField.length in 3..100, ValidationError.FULL_NAME_INVALID_LENGTH)

    requireValidationError(trimmedField.contains(" "), ValidationError.FULL_NAME_LAST_NAME_MISSING)


    requireValidationError(
        trimmedField.matches(Regex("^\\p{L}+(?:[-\\s]\\p{L}+)*$")),
        ValidationError.FULL_NAME_INVALID_CHARACTERS
    )
}

@Throws(FieldValidationException::class)
fun validateName(field: String) {
    requireValidationError(field.isNotBlank(), ValidationError.NAME_NULL_OR_EMPTY)

    requireValidationError(field.length in 2..50, ValidationError.NAME_INVALID_LENGTH)

    requireValidationError(
        field.matches(Regex("^\\p{L}+([-]\\p{L}+)*$")),
        ValidationError.NAME_INVALID_CHARACTERS
    )
}


@Throws(FieldValidationException::class)
fun validatePhoneNumber(field: String?) {
    requireValidationError(!field.isNullOrBlank(), ValidationError.PHONE_NUMBER_NULL_OR_EMPTY)

    if (!isPhoneValid(field)) {
        val cleanField = field.keepNumbersAndAlphabets()
        val fieldError = when {

            cleanField.length < 10 -> ValidationError.PHONE_NUMBER_LESS_THAN_10_DIGITS
            cleanField.length > 10 -> ValidationError.PHONE_NUMBER_MORE_THAN_10_DIGITS
            else -> ValidationError.INVALID_PHONE_NUMBER_FORMAT
        }

        requireValidationError(false, fieldError)
    }
}

fun isValidIndianPincode(pincode: String): Boolean {
    val pincodeRegex = Regex("^[1-9][0-9]{5}\$")

    return pincode.matches(pincodeRegex)
}

@OptIn(ExperimentalContracts::class)
fun requireValidationError(condition: Boolean, validationError: ValidationError) {
    contract {
        returns() implies condition
    }
    if (!condition) {
        throw ValidationException(validationError = validationError)
    }
}

open class ValidationException(val validationError: ValidationError) :
    Exception(validationError.description)


