package com.oguzdogdu.wallieshd.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.*
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FlowObserver<T>(
    lifecycleOwner: LifecycleOwner,
    private val flow: Flow<T>,
    private val collector: suspend (T) -> Unit
) {

    private var job: Job? = null

    init {
        lifecycleOwner.lifecycle.addObserver(
            LifecycleEventObserver { source: LifecycleOwner, event: Lifecycle.Event ->
                when (event) {
                    ON_START -> {
                        job = source.lifecycleScope.launch {
                            flow.collect { collector(it) }
                        }
                    }

                    ON_RESUME -> {
                        job = source.lifecycleScope.launch {
                            flow.collect { collector(it) }
                        }
                    }

                    ON_STOP -> {
                        job?.cancel()
                        job = null
                    }

                    else -> {}
                }
            }
        )
    }
}

inline fun <reified T> Flow<T>.observeInLifecycle(
    lifecycleOwner: LifecycleOwner,
    crossinline observer: suspend (T) -> Unit
) = FlowObserver(lifecycleOwner, flow = this, collector = {
    this.collect { t ->
        observer(t)
    }
})
