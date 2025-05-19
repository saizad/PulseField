package com.pulse.field

class IntField(
    fieldId: String,
    isMandatory: Boolean = false,
    ogField: Int? = null,
) : FormField<Int>(id = fieldId, isMandatory = isMandatory, ogField = ogField)