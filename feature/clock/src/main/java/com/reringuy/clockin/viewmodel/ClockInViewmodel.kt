package com.reringuy.clockin.viewmodel

import androidx.lifecycle.viewModelScope
import com.reringuy.clockin.reducer.ClockReducer
import com.reringuy.mvi.BaseViewmodel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ClockInViewmodel @Inject constructor(): BaseViewmodel<ClockReducer.ClockState, ClockReducer.ClockEvents, ClockReducer.ClockEffects>(
    initialState = ClockReducer.ClockState.initial,
    reducer = ClockReducer()
) {

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