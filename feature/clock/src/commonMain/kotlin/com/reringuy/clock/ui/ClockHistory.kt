package com.reringuy.clock.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.reringuy.clock.reducer.ClockHistoryReducer
import com.reringuy.clock.utils.formatInstantToDateTime
import com.reringuy.clock.utils.formatLocalDateToDate
import com.reringuy.clock.viewmodel.ClockHistoryViewmodel
import com.reringuy.database.relation.ClockWithHours
import com.reringuy.utils.OperationHandler
import com.reringuy.utils.components.Loading
import com.reringuy.utils.mvi.rememberFlowWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

@Composable
fun ClockHistoryWrapper(modifier: Modifier, viewmodel: ClockHistoryViewmodel = koinViewModel<ClockHistoryViewmodel>()) {
    val state by viewmodel.state.collectAsState()
    val effects = rememberFlowWithLifecycle(viewmodel.effect)

    LaunchedEffect(effects) {
        effects.collect {
            when (it) {
                is ClockHistoryReducer.ClockHistoryEffect.OnError -> {
//                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    if (state.clockHours is OperationHandler.Success)
        ClockHistoryScreen(
            modifier = modifier,
            clockHours = (state.clockHours as OperationHandler.Success<List<ClockWithHours>>).data
        )
    else
        Loading()
}

@Composable
fun ClockHistoryScreen(modifier: Modifier, clockHours: List<ClockWithHours>) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
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
                items(clockHours) { clockWithHour ->
                    ClockHistoryItem(clockWithHours = clockWithHour)
                }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun ClockHistoryItem(clockWithHours: ClockWithHours) {
    val clockDate = formatLocalDateToDate(clockWithHours.clock.dateStart)
    clockWithHours.clockHours.forEach { clockHour ->
        val (date, hour) = formatInstantToDateTime(clockHour.date)
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 8.dp)) {
            Row(modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                    Text(text = "Ponto do dia:", style = MaterialTheme.typography.titleMedium)
                    Text(text = clockDate, style = MaterialTheme.typography.titleMedium)
                }
                VerticalDivider()
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = date, style = MaterialTheme.typography.titleMedium)
                        Text(text = hour, style = MaterialTheme.typography.titleMedium)
                    }
                    Text(
                        text = clockHour.type.label,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}