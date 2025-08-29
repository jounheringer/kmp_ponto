package com.reringuy.composeapp

import android.app.Application
import com.reringuy.clock.models.clockHistoryModule
import com.reringuy.clock.models.clockModule
import com.reringuy.database.module.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(
                databaseModule,
                clockModule,
                clockHistoryModule
            )
        }
    }
}