package com.pulse.field

open class StringField(
    id: String,
    isMandatory: Boolean = false,
    ogField: String? = null,
) : FormField<String>(id = id, isMandatory = isMandatory, ogField = ogField) {

    override fun updateField(value: String?) {
        super.updateField(if (value.isNullOrEmpty()) null else value)
    }
}