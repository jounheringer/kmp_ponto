package com.reringuy.composeapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.reringuy.clock.models.clockHistoryModule
import com.reringuy.clock.models.clockModule
import com.reringuy.database.module.databaseModule
import org.koin.core.context.startKoin

fun main() = application {
    startKoin {
        modules(
            databaseModule,
            clockModule,
            clockHistoryModule
        )
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "Ponto",
    ) {
        App()
    }
}