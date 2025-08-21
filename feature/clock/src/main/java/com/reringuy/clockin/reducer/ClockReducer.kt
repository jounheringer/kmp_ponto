package com.reringuy.clockin.reducer

import com.reringuy.database.dto.ClockWithHours
import com.reringuy.mvi.Reducer
import com.reringuy.mvi.utils.OperationHandler

class ClockReducer : Reducer<
        ClockReducer.ClockState,
        ClockReducer.ClockEvents,
        ClockReducer.ClockEffects> {
    sealed class ClockEvents() : Reducer.ViewEvent {
        data object SetDataVisible : ClockEvents()
        data object SetHistoryExpanded : ClockEvents()
        data class SetTodayClock(val todayClock: OperationHandler<ClockWithHours>) : ClockEvents()
    }

    sealed class ClockEffects() : Reducer.ViewEffect {
        data class OnError(val message: String) : ClockEffects()
    }

    data class ClockState(
        val loading: Boolean,
        val dataVisible: Boolean,
        val historyExpanded: Boolean,
        val todayClock: OperationHandler<ClockWithHours>
    ) : Reducer.ViewState {
        companion object {
            val initial = ClockState(
                loading = false,
                dataVisible = false,
                historyExpanded = false,
                todayClock = OperationHandler.Waiting
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

            is ClockEvents.SetTodayClock -> {
                if (event.todayClock is OperationHandler.Failure) {
                    return Pair(
                        previousState.copy(todayClock = event.todayClock),
                        ClockEffects.OnError(event.todayClock.message)
                    )
                }
                return Pair(previousState.copy(todayClock = event.todayClock), null)
            }
        }
    }
}