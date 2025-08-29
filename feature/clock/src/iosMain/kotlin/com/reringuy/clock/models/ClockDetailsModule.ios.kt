package com.reringuy.clock.models

import com.reringuy.clock.viewmodel.ClockInViewmodel
import org.koin.dsl.module

actual val clockModule = module {
    factory { ClockInViewmodel(get()) }
}