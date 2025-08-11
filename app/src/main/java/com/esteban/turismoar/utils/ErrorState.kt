package com.esteban.turismoar.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class ErrorState {
    val error: MutableState<Throwable?> = mutableStateOf(null)

    fun setError(throwable: Throwable) {
        error.value = throwable
    }

    fun clear() {
        error.value = null
    }
}