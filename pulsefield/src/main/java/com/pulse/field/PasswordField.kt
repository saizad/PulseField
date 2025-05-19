package com.pulse.field

open class PasswordField(
    id: String = "password",
    isMandatory: Boolean = true,
    ogField: String? = null,
) : StringField(id = id, isMandatory = isMandatory, ogField = ogField)