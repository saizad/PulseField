package com.pulse.field

open class DateField(
    id: String,
    isMandatory: Boolean = false,
    ogField: String? = null,
) : StringField(id = id, isMandatory = isMandatory, ogField = ogField)