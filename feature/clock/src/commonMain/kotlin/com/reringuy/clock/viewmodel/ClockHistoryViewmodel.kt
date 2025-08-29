package com.reringuy.clock.viewmodel

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.reringuy.clock.reducer.ClockHistoryReducer
import com.reringuy.database.dao.ClockDao
import com.reringuy.utils.OperationHandler
import com.reringuy.utils.mvi.BaseViewmodel
import kotlinx.coroutines.launch

class ClockHistoryViewmodel(
    private val clockDao: ClockDao,
) : BaseViewmodel<ClockHistoryReducer.ClockHistoryState, ClockHistoryReducer.ClockHistoryEvent, ClockHistoryReducer.ClockHistoryEffect>(
    ClockHistoryReducer.ClockHistoryState.initialState, ClockHistoryReducer()
) {

    init {
        loadClockHistory()
    }

    private fun loadClockHistory() {
        sendEvent(ClockHistoryReducer.ClockHistoryEvent.LoadClockHistory(OperationHandler.Loading))
        viewModelScope.launch {
            try {
                val clockHours = clockDao.getAll()
                sendEvent(
                    ClockHistoryReducer.ClockHistoryEvent.LoadClockHistory(
                        OperationHandler.Success(clockHours)
                    )
                )
            } catch (e: Exception) {
                Logger.e("ClockHistoryViewmodel") { "loadClockHistory: ${e.message}" }
            }
        }
    }
}