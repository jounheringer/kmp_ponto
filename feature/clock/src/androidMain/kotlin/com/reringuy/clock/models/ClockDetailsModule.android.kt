package com.reringuy.clock.models

import com.reringuy.clock.viewmodel.ClockInViewmodel
import com.reringuy.database.PontoDatabase
import com.reringuy.database.getDatabaseBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val clockModule = module {
    single { getDatabaseBuilder(context = androidContext()) }
    single { get<PontoDatabase>().clockDao() }
    factory { ClockInViewmodel(get()) }
}