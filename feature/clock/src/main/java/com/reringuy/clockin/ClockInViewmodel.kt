package com.reringuy.clockin

import com.reringuy.mvi.BaseViewmodel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class ClockInViewmodel @Inject constructor(): BaseViewmodel<ClockReducer.ClockState, ClockReducer.ClockEvents, ClockReducer.ClockEffects>(
    initialState = ClockReducer.ClockState.initial,
    reducer = ClockReducer()
) {
}