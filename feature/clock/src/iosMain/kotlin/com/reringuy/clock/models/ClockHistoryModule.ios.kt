package com.reringuy.clock.models

import com.reringuy.clock.viewmodel.ClockHistoryViewmodel
import com.reringuy.database.PontoDatabase
import com.reringuy.database.getDatabaseBuilder
import org.koin.dsl.module

actual val clockHistoryModule = module {
    single { getDatabaseBuilder() }
    single { get<PontoDatabase>().clockDao() }
    factory { ClockHistoryViewmodel(get()) }
}