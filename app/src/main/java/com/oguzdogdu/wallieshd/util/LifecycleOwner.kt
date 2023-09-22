package com.oguzdogdu.wallieshd.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

inline fun <reified T : Any> LifecycleOwner.observe(
    stateFlow: StateFlow<T>,
    lifecycleOwner: LifecycleOwner,
    crossinline observer: (T) -> Unit
): Flow<T> = flow<T> {
    lifecycleOwner.lifecycle.coroutineScope.launch {
        stateFlow.collect { t ->
            observer(t)
        }
    }
}

fun <T> StateFlow<T>.collectAsFlow(param: (T) -> Unit, lifecycleOwner: LifecycleOwner): Flow<T> = flow {
    lifecycleOwner.lifecycleScope.launch {
        collect { value ->
            emit(value)
        }
    }
}
