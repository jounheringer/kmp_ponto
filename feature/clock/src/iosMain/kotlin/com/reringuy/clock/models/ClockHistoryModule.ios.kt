package com.reringuy.clock.models

import com.reringuy.clock.viewmodel.ClockHistoryViewmodel
import org.koin.dsl.module

actual val clockHistoryModule = module {
    factory { ClockHistoryViewmodel(get()) }
}