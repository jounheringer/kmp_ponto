package com.reringuy.shared.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes(val title: String) {
    @Serializable
    data object Menu : Routes(title = "Menu")

    @Serializable
    data object Schedule : Routes(title = "Alarme")

    @Serializable
    data object Card : Routes(title = "Cartão")

    @Serializable
    data object Profile : Routes(title = "Perfil")

}