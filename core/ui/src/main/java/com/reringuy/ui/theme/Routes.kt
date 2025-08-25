package com.reringuy.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

@Serializable
sealed class Routes(val title: String, val icon: ImageVector) {
    @Serializable
    data object Menu : Routes(icon = Icons.Rounded.Home, title = "Menu")

    @Serializable
    data object Schedule : Routes(icon = Icons.Filled.Schedule, title = "Alarme")

    @Serializable
    data object Card : Routes(icon = Icons.Rounded.CreditCard, title = "Cartão")

    @Serializable
    data object Profile : Routes(icon = Icons.Rounded.Person, title = "Perfil")

}