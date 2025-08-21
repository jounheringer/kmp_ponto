package com.reringuy.clockin

import com.reringuy.mvi.Reducer

class ClockReducer : Reducer<
        ClockReducer.ClockState,
        ClockReducer.ClockEvents,
        ClockReducer.ClockEffects> {
    sealed class ClockEvents() : Reducer.ViewEvent {}
    sealed class ClockEffects() : Reducer.ViewEffect {}
    data class ClockState(
        val loading: Boolean
    ) : Reducer.ViewState {
        companion object {
            val initial = ClockState(
                loading = false
            )
        }

    }

    override fun reduce(
        previousState: ClockState,
        event: ClockEvents
    ): Pair<ClockState, ClockEffects?> {
        when (event) {

            else -> {
                return Pair(previousState, null)
            }
        }
    }
}