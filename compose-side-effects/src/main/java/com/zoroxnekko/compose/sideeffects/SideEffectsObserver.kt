package com.zoroxnekko.compose.sideeffects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Composable
fun <T> Flow<T>.CollectWithLifecycle(key1: Any? = null, key2: Any? = null, onEffect: (T) -> Unit) {
    EffectsObserver(effects = this, key1 = key1, key2 = key2, onEffect = onEffect)
}

@Composable
fun <T> EffectsObserver(
    effects: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEffect: (T) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle, key1, key2) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                effects.collect(onEffect)
            }
        }
    }
}
