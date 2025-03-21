package com.zoroxnekko.compose.sideeffects.demo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface DemoSideEffects {
    data class ShowToast(val message: String) : DemoSideEffects
}

data class DemoState(
    val count: Int = 0
)

class DemoViewModel : ViewModel() {
    private val _state = MutableStateFlow(DemoState())
    val state = _state.asStateFlow()

    private val _effects = Channel<DemoSideEffects>()
    val effects = _effects.receiveAsFlow()

    fun incrementCount() {
        viewModelScope.launch {
            _state.update { it.copy(count = it.count + 1) }
            val count = _state.value.count
            if (count % 5 == 0) {
                _effects.send(DemoSideEffects.ShowToast(message = "Count is now $count"))
            }
        }
    }
}