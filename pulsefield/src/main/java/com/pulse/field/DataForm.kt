package com.pulse.field

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take

abstract class DataForm<T>(
    id: String,
    val formFields: List<FormField<*>>,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) : FormField<T>(id, false, null) {


    override val validationState: Flow<ValidationState> =
        combine(formFields.map { it.validationState }) { states ->
            if (states.any { it is ValidationState.Loading }) {
                ValidationState.Loading
            } else {
                val errors = states.filterIsInstance<ValidationState.Error>()
                if (errors.isNotEmpty()) ValidationState.Error(
                    CompositeValidationException(
                        id,
                        errors.flatMap { it.errors.exceptions },
                        errors.joinToString { it.errors.errorMessage })
                )
                else ValidationState.Success
            }
        }
            .stateIn(
                CoroutineScope(Dispatchers.Default),
                SharingStarted.Lazily,
                null
            )
            .filterNotNull()


    override val isValid = validationState
        .map { it is ValidationState.Success }

    override fun validate(value: T?): Flow<List<FieldValidationException>> {
        return validationState.flatMapLatest {
            flow {
                if (it is ValidationState.Success) emit(emptyList()) else emit(it.errors)
            }
        }
    }

    override val isModified: Flow<Boolean> =
        combine(formFields.map { it.isModified }) { modifications ->
            modifications.any { it }
        }

    override fun clear() {
        formFields.forEach { it.clear() }
    }

    override fun reset() {
        formFields.forEach { it.reset() }
    }


    fun updateBuild(): Flow<T> {
       return build().take(1)
            .onEach {
                updateField(it)
            }
    }

    abstract fun build(): Flow<T>

    inline fun <reified F : FormField<*>> requiredFindField(fieldName: String): F {
        formFields.forEach {
            if (it.id.equals(fieldName, true)) {
                return it as F
            }
        }
        throw IllegalStateException("$fieldName form field not found")
    }

    fun fieldsFlow(): Flow<Map<String, Any?>> {
        val fieldFlows = this.formFields.map { it.id to it.fieldFlow }
        val ids = fieldFlows.map { it.first }
        val flows = fieldFlows.map { it.second }

        return combine(flows) { values ->
            ids.zip(values).toMap()
        }
    }
}

