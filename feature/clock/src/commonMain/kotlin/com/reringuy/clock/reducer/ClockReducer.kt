package com.reringuy.clock.reducer

import com.reringuy.clock.reducer.ClockReducer.ClockEffects.OnError
import com.reringuy.clock.reducer.ClockReducer.ClockEffects.OnTodayClockLoaded
import com.reringuy.database.entities.ClockHour
import com.reringuy.database.relation.ClockWithHours
import com.reringuy.utils.OperationHandler
import com.reringuy.utils.mvi.Reducer

class ClockReducer : Reducer<
        ClockReducer.ClockState,
        ClockReducer.ClockEvents,
        ClockReducer.ClockEffects> {
    sealed class ClockEvents() : Reducer.ViewEvent {
        data object SetDataVisible : ClockEvents()
        data object SetHistoryExpanded : ClockEvents()
        data class SetTodayClock(val todayClock: OperationHandler<ClockWithHours>) : ClockEvents()
        data class SetClockHour(val clockHour: ClockHour) : ClockEvents()
        data class SetBankedHours(val bankedHours: String) : ClockEvents()
        data class SetWorkedHours(val workedHours: String) : ClockEvents()
    }

    sealed class ClockEffects() : Reducer.ViewEffect {
        data class OnError(val message: String) : ClockEffects()
        data class OnTodayClockLoaded(val todayClock: ClockWithHours) : ClockEffects()
    }

    data class ClockState(
        val loading: Boolean,
        val dataVisible: Boolean,
        val historyExpanded: Boolean,
        val todayClock: OperationHandler<ClockWithHours>,
        val workedHours: String,
        val bankedHours: String
    ) : Reducer.ViewState {
        companion object {
            val initial = ClockState(
                loading = false,
                dataVisible = false,
                historyExpanded = false,
                todayClock = OperationHandler.Waiting,
                workedHours = "00:00",
                bankedHours = "00:00"
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
                        OnError(event.todayClock.message)
                    )
                }
                if (event.todayClock is OperationHandler.Success)
                    return Pair(
                        previousState.copy(todayClock = event.todayClock),
                        OnTodayClockLoaded(event.todayClock.data)
                    )
                return Pair(previousState.copy(todayClock = event.todayClock), null)
            }

            is ClockEvents.SetClockHour -> {
                if (previousState.todayClock is OperationHandler.Success) {
                    val updatedTodayClock = previousState.todayClock.data.copy(
                        clockHours = previousState.todayClock.data.clockHours.plus(event.clockHour)
                    )
                    return Pair(
                        previousState.copy(
                            todayClock = OperationHandler.Success(updatedTodayClock)
                        ), null
                    )
                } else {
                    return Pair(
                        previousState.copy(
                            todayClock = OperationHandler.Failure("Erro ao registrar ponto")
                        ), OnError(
                            "Erro ao registrar ponto"
                        )
                    )
                }
            }

            is ClockEvents.SetBankedHours -> {
                return previousState.copy(bankedHours = event.bankedHours) to null
            }

            is ClockEvents.SetWorkedHours -> {
                return previousState.copy(workedHours = event.workedHours) to null
            }
        }
    }
}