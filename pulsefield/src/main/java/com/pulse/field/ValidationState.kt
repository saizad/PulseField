package com.pulse.field

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

sealed class ValidationState {
    data object Loading : ValidationState()
    data object Success : ValidationState()
    data class Error(val errors: CompositeValidationException) : ValidationState()
}

val ValidationState.errors: List<FieldValidationException>
    get() = (this as? ValidationState.Error)?.errors?.exceptions ?: emptyList()

val Flow<ValidationState>.filterLoading: Flow<ValidationState.Loading> get() = filterIsInstance()
val Flow<ValidationState>.filterError: Flow<ValidationState.Error> get() = filterIsInstance()
val Flow<ValidationState>.filterSuccess: Flow<ValidationState.Success> get() = filterIsInstance()

val Flow<ValidationState.Error>.errors get() = map { it.errors }

val Flow<ValidationState>.isLoading: Flow<Boolean> get() = map { it is ValidationState.Loading }

val FormField<*>.error: Flow<String> get() = validationState.filterError.errors
    .map { it.exceptions }
    .map { exceptions ->
        exceptions.mapNotNull { exception ->
            exception.message?.let { msg ->
                exception.description?.let { desc -> "$msg($desc)" } ?: msg
            }
        }.joinToString()
    }