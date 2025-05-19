package com.pulse.field

open class LongField(
    id: String,
    isMandatory: Boolean = false,
    ogField: Long? = null,
) : FormField<Long>(id = id, isMandatory = isMandatory, ogField = ogField)