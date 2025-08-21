package com.reringuy.clockin.reducer

import com.reringuy.mvi.Reducer

class ClockReducer : Reducer<
        ClockReducer.ClockState,
        ClockReducer.ClockEvents,
        ClockReducer.ClockEffects> {
    sealed class ClockEvents() : Reducer.ViewEvent {
        data object SetDataVisible : ClockEvents()
        data object SetHistoryExpanded : ClockEvents()
    }
    sealed class ClockEffects() : Reducer.ViewEffect {}
    data class ClockState(
        val loading: Boolean,
        val dataVisible: Boolean,
        val historyExpanded: Boolean
    ) : Reducer.ViewState {
        companion object {
            val initial = ClockState(
                loading = false,
                dataVisible = false,
                historyExpanded = false
            )
        }

    }

    override fun reduce(
        previousState: ClockState,
        event: ClockEvents
    ): Pair<ClockState, ClockEffects?> {
        when (event) {
            ClockEvents.SetDataVisible -> {
                return previousState.copy(dataVisible = !previousState.dataVisible) to null
            }

            ClockEvents.SetHistoryExpanded -> {
                return previousState.copy(historyExpanded = !previousState.historyExpanded) to null
            }
        }
    }
}