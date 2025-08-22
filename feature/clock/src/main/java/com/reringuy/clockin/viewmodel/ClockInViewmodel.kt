package com.reringuy.clockin.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.reringuy.clockin.reducer.ClockReducer
import com.reringuy.clockin.utils.formatDurationToHoursAndMinutes
import com.reringuy.clockin.utils.formatInstantToYearAndMonth
import com.reringuy.database.dao.PontoDao
import com.reringuy.database.dto.ClockWithHours
import com.reringuy.database.models.ClockHour
import com.reringuy.database.models.ClockHourType
import com.reringuy.mvi.BaseViewmodel
import com.reringuy.mvi.utils.OperationHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.Instant
import java.time.LocalDate

@HiltViewModel
class ClockInViewmodel @Inject constructor(
    private val clockDao: PontoDao
) : BaseViewmodel<ClockReducer.ClockState, ClockReducer.ClockEvents, ClockReducer.ClockEffects>(
    initialState = ClockReducer.ClockState.initial,
    reducer = ClockReducer()
) {
    init {
        loadTodayClock()
        getBankedHours()
    }

    private fun loadTodayClock() {
        sendEvent(ClockReducer.ClockEvents.SetTodayClock(OperationHandler.Loading))
        viewModelScope.launch {
            try {
                clockDao.getClockByDate(LocalDate.now()).collect {
                    if (it != null)
                        sendEventForEffect(
                            ClockReducer.ClockEvents.SetTodayClock(
                                OperationHandler.Success(
                                    it
                                )
                            )
                        )
                    else {
                        val newClock = ClockWithHours.new()
                        withContext(Dispatchers.IO) {
                            newClock.clock.uid = clockDao.insertClock(newClock.clock)
                        }
                        sendEventForEffect(
                            ClockReducer.ClockEvents.SetTodayClock(
                                OperationHandler.Success(newClock)
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("ClockInViewmodel", "loadTodayClock: ${e.message}")
                sendEventForEffect(ClockReducer.ClockEvents.SetTodayClock(OperationHandler.Failure("Erro ao carregar ponto")))
            }
        }
    }

    private fun getBankedHours() {
        viewModelScope.launch {
            try {
                val currentYearMonth = formatInstantToYearAndMonth(Instant.now())
                var saldoTotal = Duration.ZERO
                clockDao.getClocksByMonth(currentYearMonth).collect { clockWithHours ->
                    clockWithHours.forEach { (_, clockHours) ->
                        var workedHourDay = Duration.ZERO
                        val sortedClockHours = clockHours.sortedBy { it.date }

                        val timeIntervals = mutableMapOf<ClockHour, Instant>()

                        if (sortedClockHours.size > 1) {
                            sortedClockHours.forEachIndexed { index, hour ->
                                if (hour.type == ClockHourType.ENTRADA && sortedClockHours.size > index + 1)
                                    timeIntervals[hour] = sortedClockHours[index + 1].date
                            }

                            timeIntervals.forEach { (checkIn, checkOut) ->
                                if (checkIn.type == ClockHourType.ENTRADA)
                                    workedHourDay += Duration.between(checkIn.date, checkOut)
                            }
                        } else
                            workedHourDay -= Duration.ofHours(8)

                        saldoTotal += workedHourDay.minusHours(8)
                    }
                    sendEvent(
                        ClockReducer.ClockEvents.SetBankedHours(
                            formatDurationToHoursAndMinutes(saldoTotal)
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e("ClockInViewmodel", "loadTodayClock: ${e.message}")
                sendEffect(ClockReducer.ClockEffects.OnError("Erro ao carregar ponto"))
            }
        }
    }

    fun getWorkedHours(clockWithHours: ClockWithHours) {
        viewModelScope.launch {
            try {
                val sortedClockHours = clockWithHours.clockHours.sortedBy { it.date }
                var workedHourDay = Duration.ZERO
                val timeIntervals = mutableMapOf<Instant, Instant>()

                sortedClockHours.forEachIndexed { index, hour ->
                    if (hour.type == ClockHourType.ENTRADA && sortedClockHours.size > index + 1) {
                        timeIntervals[hour.date] = sortedClockHours[index + 1].date
                    }
                }

                timeIntervals.forEach { (checkIn, checkOut) ->
                    workedHourDay += Duration.between(checkIn, checkOut)
                }
                sendEvent(
                    ClockReducer.ClockEvents.SetWorkedHours(
                        formatDurationToHoursAndMinutes(workedHourDay.minusHours(8))
                    )
                )
            } catch (e: Exception) {
                Log.e("ClockInViewmodel", "loadTodayClock: ${e.message}")
                sendEffect(ClockReducer.ClockEffects.OnError("Erro ao carregar ponto"))
            }
        }
    }

    fun setDataVisible() {
        viewModelScope.launch {
            sendEvent(ClockReducer.ClockEvents.SetDataVisible)
        }
    }

    fun setExpandedHistory() {
        viewModelScope.launch {
            sendEvent(ClockReducer.ClockEvents.SetHistoryExpanded)
        }
    }

    fun setClockHour() {
        viewModelScope.launch {
            val todayClock = state.value.todayClock
            if (todayClock is OperationHandler.Success) {
                val lastClockHour = todayClock.data.clockHours.lastOrNull()
                val newType = when (lastClockHour?.type) {
                    ClockHourType.ENTRADA -> ClockHourType.SAIDA
                    ClockHourType.SAIDA -> ClockHourType.ENTRADA
                    null -> ClockHourType.ENTRADA
                }
                val newClockHour = ClockHour.new(todayClock.data.clock.uid, type = newType)
                withContext(Dispatchers.IO) {
                    newClockHour.uid = clockDao.insertClockHour(newClockHour)
                }
                sendEvent(ClockReducer.ClockEvents.SetClockHour(newClockHour))
                getWorkedHours(todayClock.data)
                getBankedHours()
            } else
                sendEffect(ClockReducer.ClockEffects.OnError("Erro ao registrar ponto"))
        }
    }
}