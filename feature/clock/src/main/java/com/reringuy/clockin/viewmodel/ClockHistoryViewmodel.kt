package com.reringuy.clockin.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.reringuy.clockin.reducer.ClockHistoryReducer
import com.reringuy.clockin.reducer.ClockHistoryReducer.ClockHistoryEffect
import com.reringuy.clockin.reducer.ClockHistoryReducer.ClockHistoryEvent
import com.reringuy.clockin.reducer.ClockHistoryReducer.ClockHistoryState
import com.reringuy.database.dao.PontoDao
import com.reringuy.mvi.BaseViewmodel
import com.reringuy.mvi.utils.OperationHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class ClockHistoryViewmodel @Inject constructor(
    private val clockDao: PontoDao
) : BaseViewmodel<ClockHistoryState, ClockHistoryEvent, ClockHistoryEffect>(
    ClockHistoryState.initialState, ClockHistoryReducer()
) {

    init {
        loadClockHistory()
    }

    private fun loadClockHistory() {
        sendEvent(ClockHistoryEvent.LoadClockHistory(OperationHandler.Loading))
        viewModelScope.launch {
            try {
                clockDao.getClocks().collectLatest { clockHours ->
                    sendEvent(ClockHistoryEvent.LoadClockHistory(OperationHandler.Success(clockHours)))
                }
            } catch (e: Exception) {
                Log.e("ClockHistoryViewmodel", "loadClockHistory: ${e.message}")
            }
        }
    }
}