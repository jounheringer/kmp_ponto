package com.reringuy.clock.viewmodel

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.reringuy.clock.reducer.ClockReducer
import com.reringuy.clock.utils.formatDurationToHoursAndMinutes
import com.reringuy.clock.utils.formatInstantToYearAndMonth
import com.reringuy.database.dao.ClockDao
import com.reringuy.database.entities.ClockHour
import com.reringuy.database.entities.ClockHourType
import com.reringuy.database.relation.ClockWithHours
import com.reringuy.utils.OperationHandler
import com.reringuy.utils.mvi.BaseViewmodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class ClockInViewmodel(
    private val clockDao: ClockDao,
) : BaseViewmodel<ClockReducer.ClockState, ClockReducer.ClockEvents, ClockReducer.ClockEffects>(
    initialState = ClockReducer.ClockState.initial,
    reducer = ClockReducer()
) {
    init {
        loadTodayClock()
        getBankedHours()
    }

    @OptIn(ExperimentalTime::class)
    private fun loadTodayClock() {
        sendEvent(ClockReducer.ClockEvents.SetTodayClock(OperationHandler.Loading))
        viewModelScope.launch {
            try {
                val currentDate  = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

                val clockFull = clockDao.getClockByDate(currentDate)
                if (clockFull != null)
                    sendEventForEffect(
                        ClockReducer.ClockEvents.SetTodayClock(
                            OperationHandler.Success(
                                clockFull
                            )
                        )
                    )
                else {
                    val newClock = ClockWithHours.new()
                    withContext(Dispatchers.IO) {
                        newClock.clock.uid = clockDao.insertClock(newClock.clock)
                        sendEventForEffect(
                            ClockReducer.ClockEvents.SetTodayClock(
                                OperationHandler.Success(newClock)
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                Logger.e("ClockInViewmodel") { "loadTodayClock: ${e.message}" }
                sendEventForEffect(ClockReducer.ClockEvents.SetTodayClock(OperationHandler.Failure("Erro ao carregar ponto")))
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun getBankedHours() {
        viewModelScope.launch {
            try {
                val currentYearMonth = formatInstantToYearAndMonth(Clock.System.now())
                val clockWithHours = clockDao.getClocksByMonth(currentYearMonth)
                var saldoTotal = Duration.ZERO
                val sortedClockHours =
                    clockWithHours.flatMap { it.clockHours }.sortedBy { it.date }
                var workedHourDay = Duration.ZERO
                val timeIntervals = mutableMapOf<ClockHour, Instant>()

                sortedClockHours.forEachIndexed { index, clockHour ->
                    if (clockHour.type == ClockHourType.ENTRADA && sortedClockHours.size > index + 1)
                        timeIntervals[clockHour] = sortedClockHours[index + 1].date
                }

                timeIntervals.forEach { (checkIn, checkOut) ->
                    if (checkIn.type == ClockHourType.ENTRADA)
                        workedHourDay += (checkIn.date - checkOut)
                }

                saldoTotal += workedHourDay.minus(8.hours)
                sendEvent(
                    ClockReducer.ClockEvents.SetBankedHours(
                        formatDurationToHoursAndMinutes(saldoTotal)
                    )
                )

            } catch (e: Exception) {
                Logger.e("ClockInViewmodel") { "loadTodayClock: ${e.message}" }
                sendEffect(ClockReducer.ClockEffects.OnError("Erro ao carregar ponto"))
            }
        }
    }

    @OptIn(ExperimentalTime::class)
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
                    workedHourDay += (checkIn - checkOut)
                }
                sendEvent(
                    ClockReducer.ClockEvents.SetWorkedHours(
                        formatDurationToHoursAndMinutes(workedHourDay.minus(8.hours))
                    )
                )
            } catch (e: Exception) {
                Logger.e("ClockInViewmodel") { "loadTodayClock: ${e.message}" }
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
                    sendEvent(ClockReducer.ClockEvents.SetClockHour(newClockHour))
                    getWorkedHours(todayClock.data)
                    getBankedHours()
                }
            } else
                sendEffect(ClockReducer.ClockEffects.OnError("Erro ao registrar ponto"))
        }
    }
}