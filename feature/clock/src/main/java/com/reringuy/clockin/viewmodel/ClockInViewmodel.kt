package com.reringuy.clockin.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.reringuy.clockin.reducer.ClockReducer
import com.reringuy.database.dao.PontoDao
import com.reringuy.database.dto.ClockWithHours
import com.reringuy.mvi.BaseViewmodel
import com.reringuy.mvi.utils.OperationHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
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
    }

    private fun loadTodayClock() {
        sendEvent(ClockReducer.ClockEvents.SetTodayClock(OperationHandler.Loading))
        viewModelScope.launch {
            try {
                clockDao.getClockByDate(LocalDate.now()).collect {
                    if (it != null)
                        sendEvent(ClockReducer.ClockEvents.SetTodayClock(OperationHandler.Success(it)))
                    else
                        sendEvent(
                            ClockReducer.ClockEvents.SetTodayClock(
                                OperationHandler.Success(ClockWithHours.new())
                            )
                        )
                }
            } catch (e: Exception) {
                Log.e("ClockInViewmodel", "loadTodayClock: ${e.message}")
                sendEvent(ClockReducer.ClockEvents.SetTodayClock(OperationHandler.Failure("Erro ao carregar ponto")))
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
}