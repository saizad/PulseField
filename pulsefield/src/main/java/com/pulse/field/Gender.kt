package com.pulse.field

import com.pulse.field.exts.ValidationError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

enum class Gender(val type: Int) {
    MALE(1), FEMALE(2), OTHER(0), UNSPECIFIED(-1)
}

fun Gender.displayName(): String = name.lowercase().replaceFirstChar { it.uppercase() }

fun Gender?.isMale(): Boolean = this == Gender.MALE

// Extension to convert a String to Gender
fun String.toGender(): Gender {
    return Gender.valueOf(this.uppercase())
}

// Extension to convert a String to Gender
fun Int.toGender(): Gender {
    return Gender.entries.find { it.type == this } ?: Gender.UNSPECIFIED
}

class GenderField(
    id: String,
    isMandatory: Boolean = false,
    ogField: Gender? = null
) : FormField<Gender>(id, isMandatory, ogField) {

    override fun validate(value: Gender?): Flow<List<FieldValidationException>> {
        return super.validate(value)
            .flatMapLatest {
                if (isMandatory && value == Gender.UNSPECIFIED) {
                    flowOf(
                        listOf(
                            listOf(FieldValidationException(ValidationError.INVALID_GENDER)),
                            it
                        ).flatten()
                    )
                } else {
                    flowOf(it)
                }
            }
    }
}