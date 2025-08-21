package com.reringuy.mvi

interface Reducer<State : Reducer.ViewState, Event : Reducer.ViewEvent, Effect : Reducer.ViewEffect> {
    interface ViewState
    interface ViewEvent
    interface ViewEffect

    fun reduce(previousState: State, event: Event): Pair<State, Effect?>
}