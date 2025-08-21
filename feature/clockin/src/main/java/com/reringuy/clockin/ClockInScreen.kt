package com.reringuy.clockin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reringuy.mvi.rememberFlowWithLifecycle
import com.reringuy.ui.theme.componentes.Loading

@Composable
fun ClockInScreenWrapper(viewmodel: ClockInViewmodel = hiltViewModel()) {
    val state by viewmodel.state.collectAsStateWithLifecycle()
    val effects = rememberFlowWithLifecycle(viewmodel.effect)

    if (!state.loading)
        ClockDetails()
    else
        Loading()
}

@Preview
@Composable
fun ClockDetails() {
    Card(Modifier.padding(16.dp)) {
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
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Rounded.Visibility,
                            contentDescription = "Visivel"
                        )
//                        Icon(imageVector = Icons.Rounded.VisibilityOff, contentDescription = "Invinsivel")
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Rounded.Share,
                            contentDescription = "Compartilhar"
                        )
                    }
                }
            }

            LastClock()

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                CardSecondaryDetails(modifier = Modifier.weight(1f), "Entrada", "15/08")
                CardSecondaryDetails(modifier = Modifier.weight(1f), "Saída", "15/08")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {},
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Registrar ponto", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun LastClock() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(color = MaterialTheme.colorScheme.tertiaryContainer),
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
                    text = "Registrado às: --:--",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            TextButton(
                onClick = {},
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
    }
}

@Composable
fun CardSecondaryDetails(modifier: Modifier, title: String, date: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "....", style = MaterialTheme.typography.titleMedium)
            Text(text = title, style = MaterialTheme.typography.bodyMedium)
        }
    }

}