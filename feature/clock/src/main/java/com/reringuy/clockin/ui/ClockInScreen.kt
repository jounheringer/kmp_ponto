package com.reringuy.clockin.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reringuy.clockin.reducer.ClockReducer
import com.reringuy.clockin.utils.formatInstantToDateTime
import com.reringuy.clockin.viewmodel.ClockInViewmodel
import com.reringuy.database.entities.ClockHour
import com.reringuy.database.relation.ClockWithHours
import com.reringuy.utils.OperationHandler
import com.reringuy.utils.components.Loading
import com.reringuy.utils.mvi.rememberFlowWithLifecycle
import kotlin.time.ExperimentalTime

@Composable
fun ClockInScreenWrapper(modifier: Modifier, viewmodel: ClockInViewmodel = hiltViewModel()) {
    val state by viewmodel.state.collectAsStateWithLifecycle()
    val effects = rememberFlowWithLifecycle(viewmodel.effect)
    val context = LocalContext.current
    LaunchedEffect(effects) {
        effects.collect {
            when (it) {
                is ClockReducer.ClockEffects.OnError -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }

                is ClockReducer.ClockEffects.OnTodayClockLoaded -> {
                    viewmodel.getWorkedHours(it.todayClock)
                }
            }
        }
    }

    if (state.todayClock is OperationHandler.Success) {
        val data = (state.todayClock as OperationHandler.Success<ClockWithHours>).data
        Column {
            ClockDetails(
                modifier = modifier,
                state = state,
                todayClock = data,
                setDataVisible = viewmodel::setDataVisible,
                onExpandHistory = viewmodel::setExpandedHistory,
                onRegisterClockHour = viewmodel::setClockHour
            )
        }
    } else {
        Loading()
    }
}

@Composable
fun ClockDetails(
    modifier: Modifier,
    state: ClockReducer.ClockState,
    todayClock: ClockWithHours,
    setDataVisible: () -> Unit,
    onExpandHistory: () -> Unit,
    onRegisterClockHour: () -> Unit
) {
    Card(modifier = modifier.padding(16.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Meu ponto", style = MaterialTheme.typography.titleMedium)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(onClick = setDataVisible) {
                        if (!state.dataVisible)
                            Icon(
                                imageVector = Icons.Rounded.Visibility,
                                contentDescription = "Visivel"
                            )
                        else
                            Icon(
                                imageVector = Icons.Rounded.VisibilityOff,
                                contentDescription = "Invinsivel"
                            )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Rounded.Share,
                            contentDescription = "Compartilhar"
                        )
                    }
                }
            }

            LastClock(state = state, todayClock = todayClock, onExpandHistory = onExpandHistory)

            Row(
                modifier = Modifier.height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CardSecondaryDetails(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    hidden = !state.dataVisible,
                    "Horas Trabalhadas",
                    state.workedHours
                )
                CardSecondaryDetails(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    hidden = !state.dataVisible,
                    "Banco de Horas",
                    state.bankedHours
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onRegisterClockHour,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Registrar ponto", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun LastClock(
    state: ClockReducer.ClockState,
    todayClock: ClockWithHours,
    onExpandHistory: () -> Unit
) {
    val (_, lastClockHour) = todayClock.clockHours.lastOrNull()?.date?.let {
        formatInstantToDateTime(it)
    } ?: Pair("", "--")
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = RoundedCornerShape(bottomEnd = if (state.historyExpanded) 16.dp else 0.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.AccessTime,
                    contentDescription = "Relogio",
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "Último ponto",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Registrado às: ${if (!state.dataVisible) "--:--" else lastClockHour}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            TextButton(
                onClick = onExpandHistory,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Detalhes", style = MaterialTheme.typography.labelLarge)
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Detalhes",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
            }
        }
        if (state.historyExpanded) {
            LazyColumn(modifier = Modifier.heightIn(0.dp, 200.dp)) {
                if (todayClock.clockHours.isEmpty())
                    item {
                        Text(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            text = "Nenhum ponto registrado",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                else
                    items(todayClock.clockHours.size) { index ->
                        ClockDetails(
                            hidden = !state.dataVisible,
                            clockHour = todayClock.clockHours[index]
                        )
                        if (index < todayClock.clockHours.size - 1)
                            HorizontalDivider()
                    }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun ClockDetails(hidden: Boolean, clockHour: ClockHour) {
    val (_, time) = formatInstantToDateTime(clockHour.date)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Ponto registrado às:", style = MaterialTheme.typography.titleMedium)
            Text(text = if (hidden) "--:--" else time, style = MaterialTheme.typography.titleMedium)
        }
        Text(text = clockHour.type.label, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun CardSecondaryDetails(modifier: Modifier, hidden: Boolean, title: String, date: String) {
    Card(
        modifier = modifier.height(IntrinsicSize.Min),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = if (hidden) "...." else date,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}