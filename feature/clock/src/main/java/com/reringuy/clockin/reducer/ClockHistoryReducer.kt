package com.reringuy.clockin.reducer

import com.reringuy.database.relation.ClockWithHours
import com.reringuy.utils.OperationHandler
import com.reringuy.utils.mvi.Reducer
import kotlin.time.ExperimentalTime


class ClockHistoryReducer :
    Reducer<ClockHistoryReducer.ClockHistoryState, ClockHistoryReducer.ClockHistoryEvent, ClockHistoryReducer.ClockHistoryEffect> {
    sealed class ClockHistoryEvent : Reducer.ViewEvent {
        data class LoadClockHistory(val dataList: OperationHandler<List<ClockWithHours>>) :
            ClockHistoryEvent()
    }

    sealed class ClockHistoryEffect : Reducer.ViewEffect {
        data class OnError(val error: String) : ClockHistoryEffect()
    }

    data class ClockHistoryState(
        val clockHours: OperationHandler<List<ClockWithHours>>
    ) : Reducer.ViewState {
        companion object {
            val initialState = ClockHistoryState(
                clockHours = OperationHandler.Waiting
            )
        }

    }

    @OptIn(ExperimentalTime::class)
    override fun reduce(
        previousState: ClockHistoryState,
        event: ClockHistoryEvent
    ): Pair<ClockHistoryState, ClockHistoryEffect?> {
        when (event) {
            is ClockHistoryEvent.LoadClockHistory -> {
                return when (event.dataList) {
                    is OperationHandler.Failure -> {
                        Pair(
                            previousState.copy(clockHours = event.dataList),
                            ClockHistoryEffect.OnError(event.dataList.message)
                        )
                    }

                    is OperationHandler.Success<*> -> {
                        if (previousState.clockHours is OperationHandler.Success) {
                            val eventData =
                                (event.dataList as OperationHandler.Success<List<ClockWithHours>>).data
                            val previousData = previousState.clockHours.data

                            val combinedList = eventData + previousData

                            val groupedClocks = combinedList
                                .groupBy { it.clock.uid }
                                .map { (_, clocksWithHours) ->
                                    val mergedHours = clocksWithHours.flatMap { it.clockHours }
                                        .distinctBy { it.uid }
                                        .sortedBy { it.date }
                                    clocksWithHours.first().copy(clockHours = mergedHours)
                                }
                            previousState.copy(clockHours = OperationHandler.Success(groupedClocks)) to null
                        } else
                            previousState.copy(clockHours = event.dataList) to null
                    }

                    else -> {
                        previousState.copy(clockHours = event.dataList) to null
                    }
                }
            }
        }
    }
}