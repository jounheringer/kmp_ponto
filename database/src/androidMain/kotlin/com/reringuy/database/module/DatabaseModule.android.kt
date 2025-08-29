package com.reringuy.database.module

import com.reringuy.database.PontoDatabase
import com.reringuy.database.getDatabaseBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val databaseModule = module {
    single { getDatabaseBuilder(context = androidContext()).build() }
    single { get<PontoDatabase>().clockDao() }
}