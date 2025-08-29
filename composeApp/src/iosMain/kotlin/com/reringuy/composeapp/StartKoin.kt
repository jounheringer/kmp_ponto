package com.reringuy.composeapp

import com.reringuy.clock.models.clockHistoryModule
import com.reringuy.clock.models.clockModule
import org.koin.core.context.startKoin

@Suppress("unused")
fun startIosKoin() {
    startKoin {
        modules(
            clockModule,
            clockHistoryModule
        )
    }
}