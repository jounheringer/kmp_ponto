package com.reringuy.shared.routes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
sealed class Routes(val title: String, @Contextual val icon: ImageVector) {
    @Serializable
    data object Menu : Routes(title = "Menu", icon = Icons.Filled.Home)

    @Serializable
    data object Schedule : Routes(title = "Alarme", icon = Icons.Rounded.Schedule)

    @Serializable
    data object Card : Routes(title = "Cartão", icon = Icons.Filled.CreditCard)

    @Serializable
    data object Profile : Routes(title = "Perfil", icon = Icons.Filled.Person)

}