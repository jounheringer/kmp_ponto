package com.reringuy.clockin.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reringuy.clockin.utils.formatInstantToDateTime
import com.reringuy.clockin.viewmodel.ClockHistoryViewmodel
import com.reringuy.database.dto.ClockWithHours
import com.reringuy.database.models.ClockHour
import com.reringuy.mvi.rememberFlowWithLifecycle
import com.reringuy.mvi.utils.OperationHandler
import com.reringuy.ui.theme.componentes.Loading

@Composable
fun ClockHistoryWrapper(modifier: Modifier, viewmodel: ClockHistoryViewmodel = hiltViewModel()) {
    val state by viewmodel.state.collectAsStateWithLifecycle()
    val effects = rememberFlowWithLifecycle(viewmodel.effect)

    LaunchedEffect(effects) {
        effects.collect {

        }
    }

    if (state.clockHours is OperationHandler.Success)
        ClockHistoryScreen(modifier = modifier, clockHours = (state.clockHours as OperationHandler.Success<List<ClockWithHours>>).data)
    else
        Loading()
}

@Composable
fun ClockHistoryScreen(modifier: Modifier, clockHours: List<ClockWithHours>) {
    Column(modifier = modifier
        .padding(16.dp)
        .fillMaxWidth()) {
        Text(text = "Historico de Pontos", style = MaterialTheme.typography.titleLarge)

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (clockHours.isEmpty())
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Nenhum ponto registrado",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
            else
                items(clockHours.flatMap { it.clockHours }) { clockHour ->
                    ClockHistoryItem(clockHour = clockHour)
                }
        }
    }
}

@Composable
fun ClockHistoryItem(clockHour: ClockHour) {
    val (date, hour) = formatInstantToDateTime(clockHour.date)
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = date, style = MaterialTheme.typography.bodyMedium)
                Text(text = hour, style = MaterialTheme.typography.bodyMedium)
            }
            Text(text = clockHour.type.label, style = MaterialTheme.typography.bodyMedium)
        }
    }
}