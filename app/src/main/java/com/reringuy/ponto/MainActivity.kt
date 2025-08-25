package com.reringuy.ponto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.reringuy.clockin.MainClockUI
import com.reringuy.ui.theme.PontoTheme
import com.reringuy.ui.theme.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val bottomBarRoutes = listOf(Routes.Menu, Routes.Schedule, Routes.Card, Routes.Profile)

            val windowSize = with(LocalDensity.current) {
                currentWindowSize().toSize().toDpSize()
            }

            var selectedDestination: Routes by remember { mutableStateOf(Routes.Menu) }

            val layoutType = if (windowSize.width >= 1200.dp) {
                NavigationSuiteType.NavigationDrawer
            } else {
                NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
                    currentWindowAdaptiveInfo()
                )
            }

            PontoTheme {
                NavigationSuiteScaffold(
                    layoutType = layoutType,
                    navigationSuiteItems = {
                        bottomBarRoutes.forEach {
                            item(
                                selected = it == selectedDestination,
                                onClick = { selectedDestination = it },
                                icon = {
                                    Icon(
                                        imageVector = it.icon,
                                        contentDescription = it.title
                                    )
                                },
                                label = {
                                    Text(text = it.title)
                                }
                            )
                        }
                    }
                ) {
                    MainClockUI()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PontoTheme {
        Greeting("Android")
    }
}