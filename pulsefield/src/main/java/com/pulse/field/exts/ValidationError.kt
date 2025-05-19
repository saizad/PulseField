package com.pulse.field.exts

enum class ValidationError(val description: String) {
    INVALID_EMAIL("Invalid Email"),
    MANDATORY_FIELD("Field is mandatory and value is not provided."),
    AADHAR_NUMBER_NULL_OR_EMPTY("Aadhaar number cannot be null or empty"),
    INVALID_AADHAR_NUMBER("Aadhaar number must be exactly 12 digits"),
    INVALID_AADHAR_NUMBER_FORMAT("Invalid Aadhaar number format"),
    PHONE_NUMBER_NULL_OR_EMPTY("Phone number cannot be null or empty"),
    PHONE_NUMBER_LESS_THAN_10_DIGITS("Phone number must be exactly 10 digits"),
    PHONE_NUMBER_MORE_THAN_10_DIGITS("Phone number cannot exceed 10 digits"),
    INVALID_PHONE_NUMBER_FORMAT("Invalid phone number format"),
    FULL_NAME_NULL_OR_EMPTY("Full name cannot be null or empty"),
    FULL_NAME_LAST_NAME_MISSING("Full name must contain at least first and last name"),
    FULL_NAME_INVALID_LENGTH("Full name must be between 3 and 100 characters"),
    FULL_NAME_INVALID_CHARACTERS("Full name can only contain letters, single spaces, and hyphens"),
    INVALID_GENDER("Invalid gender"),
    NAME_NULL_OR_EMPTY("First name cannot be null or empty"),
    NAME_INVALID_LENGTH("First name must be between 2 and 50 characters"),
    NAME_INVALID_CHARACTERS("First name can only contain letters and a single hyphen (if needed)"),
    INVALID_PINCODE_FORMAT("Invalid pincode format"),
    INVALID_OTP_FORMAT("Invalid otp format"),
    QUANTITY_ABOVE_MAX("Quantity exceeds maximum allowed value"),
    QUANTITY_BELOW_MIN("Quantity is below minimum allowed value"),
    NOT_FOUND("Not found"),
    INVALID_PASSWORD("Invalid password"),
    PASSWORD_NO_MATCH("Password doesn't match"),
    EMPTY_LIST("Empty list")
}