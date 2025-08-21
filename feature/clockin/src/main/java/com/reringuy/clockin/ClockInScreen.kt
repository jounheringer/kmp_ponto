package com.reringuy.clockin

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ClockInScreenWrapper() {

}

@Preview
@Composable
fun ClockInScreen() {
    Column {
        Text(text = "aaaaaaaaa", style = MaterialTheme.typography.titleLarge)
        Text(text = "bbbbbb", style = MaterialTheme.typography.bodyMedium)
    }
}