package com.reringuy.composeapp

import com.reringuy.clock.models.clockHistoryModule
import com.reringuy.clock.models.clockModule
import com.reringuy.database.module.databaseModule
import org.koin.core.context.startKoin

@Suppress("unused")
fun startIosKoin() {
    startKoin {
        modules(
            databaseModule,
            clockModule,
            clockHistoryModule
        )
    }
}