package com.reringuy.mvi

import androidx.lifecycle.ViewModel
import com.reringuy.bild_config.BuildConfig
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseViewmodel<State : Reducer.ViewState, Event : Reducer.ViewEvent, Effect : Reducer.ViewEffect>(
    initialState: State,
    private val reducer: Reducer<State, Event, Effect>
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state get() = _state.asStateFlow()

    private val _event = MutableSharedFlow<Event>()
    val event get() = _event.asSharedFlow()

    private val _effects = Channel<Effect>(capacity = Channel.CONFLATED)
    val effect get() = _effects.receiveAsFlow()

    private val timeCapsule: TimeCapsule<State> = TimeTravelCapsule { storedState ->
        _state.tryEmit(storedState)
    }

    init {
        timeCapsule.addState(initialState)
    }

    fun sendEffect(effect: Effect) {
        _effects.trySend(effect)
    }

    fun sendEvent(event: Event) {
        val (newState, _) = reducer.reduce(_state.value, event)

        val success = _state.tryEmit(newState)

        if (BuildConfig.DEBUG && success) {
            timeCapsule.addState(newState)
        }
    }

    fun sendEventForEffect(event: Event) {
        val (newState, effect) = reducer.reduce(_state.value, event)

        val success = _state.tryEmit(newState)

        if (BuildConfig.DEBUG && success) {
            timeCapsule.addState(newState)
        }

        effect?.let {
            sendEffect(it)
        }
    }
}