package com.pulse.field

import com.pulse.field.exts.ValidationError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

open class FormField<T>(
    val id: String,
    val isMandatory: Boolean,
    val ogField: T?,
) {
    private val _fieldFlow = MutableStateFlow(ogField)

    open val fieldFlow: StateFlow<T?> = _fieldFlow.asStateFlow()

    open val validationState: Flow<ValidationState> = fieldFlow
        .flatMapLatest { value ->
            flow {
                emit(ValidationState.Loading)
                val exceptions = validate(value).firstOrNull() ?: emptyList()
                emit(
                    if (exceptions.isNotEmpty()) ValidationState.Error(
                        CompositeValidationException(id, exceptions)
                    )
                    else ValidationState.Success
                )
            }
        }
        .stateIn(
            CoroutineScope(Dispatchers.Default),
            SharingStarted.Lazily,
            null
        )
        .filterNotNull()

    open val isValid = validationState
        .map { it is ValidationState.Success }

    open val isModified: Flow<Boolean> = _fieldFlow
        .map { !compare(it, ogField) }

    open fun validate(value: T?): Flow<List<FieldValidationException>> = flow {
        if (value == null && isMandatory) {
            emit(listOf(FieldValidationException(ValidationError.MANDATORY_FIELD)))
        } else {
            emit(emptyList())
        }
    }

    open fun compare(value: T?, ogField: T?): Boolean = value == ogField

    open fun clear() {
        _fieldFlow.value = null
    }

    open fun reset() {
        _fieldFlow.value = ogField
    }

    open fun updateField(value: T?) {
        _fieldFlow.value = value
    }
}