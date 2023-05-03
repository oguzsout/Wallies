package com.oguzdogdu.wallies.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

inline fun <reified T : Any> LifecycleOwner.observe(
    stateFlow: StateFlow<T>,
    lifecycleOwner: LifecycleOwner,
    crossinline observer: (T) -> Unit,
) {
    lifecycleOwner.lifecycle.coroutineScope.launch {
        stateFlow.onEach { t ->
            observer(t)
        }.observeInLifecycle(lifecycleOwner)
    }
}