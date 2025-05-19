package com.pulse.field

open class FloatField(
    id: String,
    isMandatory: Boolean = false,
    ogField: Float? = null,
) : FormField<Float>(id = id, isMandatory = isMandatory, ogField = ogField)