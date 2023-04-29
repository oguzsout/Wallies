package com.oguzdogdu.wallies.util

import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach

inline fun <reified T> LifecycleOwner.observe(
    stateFlow: StateFlow<T>,
    lifecycleOwner: LifecycleOwner,
    crossinline observer: (T) -> Unit,
) {
    stateFlow.onEach { t -> observer(t) }.observeInLifecycle(lifecycleOwner)
}