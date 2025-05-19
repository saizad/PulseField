package com.pulse.field

open class DoubleField(
    id: String,
    isMandatory: Boolean = false,
    ogField: Double? = null,
) : FormField<Double>(id = id, isMandatory = isMandatory, ogField = ogField)