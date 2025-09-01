package com.reringuy.composeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.reringuy.clock.ui.ClockHistoryWrapper
import com.reringuy.clock.ui.ClockInScreenWrapper
import com.reringuy.shared.routes.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val bottomBarRoutes = listOf(Routes.Menu, Routes.Schedule, Routes.Card, Routes.Profile)
    val windowSize = currentWindowAdaptiveInfo().windowSizeClass
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var selectedDestination: Routes by remember { mutableStateOf(Routes.Menu) }

    when {
        windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> {
            PermanentNavigationDrawer(
                modifier = Modifier.safeDrawingPadding(),
                drawerContent = {
                    Column(
                        modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier.fillMaxHeight().padding(8.dp, 16.dp).width(IntrinsicSize.Min),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            ExpandedNavigationButtons(
                                menuRoutes = bottomBarRoutes,
                                selectedRoute = selectedDestination,
                                onClickBack = {}
                            ) { selectedDestination = it }
                            Spacer(modifier = Modifier.weight(1f))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                DrawerOptions(true)
                            }
                        }
                    }
                }
            ) {
                Scaffold(topBar = { TwoPanelTopBar(scrollBehavior) }) { paddingValues ->
                    Row(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                        ClockInScreenWrapper(modifier = Modifier.fillMaxWidth(0.45f))
                        ClockHistoryWrapper(modifier = Modifier)
                    }
                }
            }
        }

        windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> {
            PermanentNavigationDrawer(
                modifier = Modifier.safeDrawingPadding(),
                drawerContent = {
                    Column(
                        modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier.fillMaxHeight().padding(8.dp, 16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            NavigationButtons(
                                menuRoutes = bottomBarRoutes,
                                selectedRoute = selectedDestination,
                                twoPanel = true,
                                onNavigateBack = {}
                            ) { selectedDestination = it }
                            Spacer(modifier = Modifier.weight(1f))
                            DrawerOptions(false)
                        }
                    }
                }
            ) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TwoPanelTopBar(scrollBehavior) }) { paddingValues ->
                    Row(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                        ClockInScreenWrapper(modifier = Modifier.fillMaxWidth(0.45f))
                        ClockHistoryWrapper(modifier = Modifier)
                    }
                }
            }
        }

        else -> {
            Scaffold(
                modifier = Modifier.safeDrawingPadding(),
                topBar = { OnePanelTopBar(scrollBehavior) },
                bottomBar = {
                    PontoBottomAppBar(
                        menuRoutes = bottomBarRoutes,
                        selectedDestination
                    ) { selectedDestination = it }
                }
            ) { innerPadding ->
                ClockInScreenWrapper(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnePanelTopBar(scrollBehavior: TopAppBarScrollBehavior) {
    TopAppBar(
        title = { Text(text = "Titulo") },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar"
                )
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Settings, contentDescription = "Configuração")
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Settings, contentDescription = "Configuração")
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwoPanelTopBar(scrollBehavior: TopAppBarScrollBehavior) {
    TopAppBar(
        title = { Text(text = "Titulo") },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun PontoBottomAppBar(
    menuRoutes: List<Routes>,
    selectedRoute: Routes,
    onRouteSelected: (Routes) -> Unit,
) {
    BottomAppBar(containerColor = MaterialTheme.colorScheme.primaryContainer) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            NavigationButtons(
                menuRoutes = menuRoutes,
                selectedRoute = selectedRoute,
                onRouteSelected = onRouteSelected
            )
        }
    }
}

@Composable
fun NavigationButtons(
    menuRoutes: List<Routes>,
    selectedRoute: Routes,
    twoPanel: Boolean = false,
    onNavigateBack: () -> Unit = {},
    onRouteSelected: (Routes) -> Unit,
) {
    if(twoPanel)
    IconButton(modifier = Modifier.padding(bottom = 12.dp), onClick = onNavigateBack) {
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = MaterialTheme.colorScheme.primary)
    }

    menuRoutes.forEach { route ->
        if (route == selectedRoute)
            Card(
                colors = CardDefaults.cardColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                IconButton(onClick = { }) {
                    Icon(imageVector = route.icon, contentDescription = route.title)
                }
            }
        else
            IconButton(onClick = { onRouteSelected(route) }) {
                Icon(imageVector = route.icon, contentDescription = route.title, tint = MaterialTheme.colorScheme.primary)
            }
    }
}

@Composable
fun ExpandedNavigationButtons(
    menuRoutes: List<Routes>,
    selectedRoute: Routes,
    onClickBack: () -> Unit,
    onRouteSelected: (Routes) -> Unit,
) {
    Card(
        modifier = Modifier.padding(bottom = 12.dp).fillMaxWidth(),
        onClick = onClickBack,
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(onClick = onClickBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar"
                )
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Voltar",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }

    menuRoutes.forEach { route ->
        if (route == selectedRoute)
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onRouteSelected(route) },
                colors = CardDefaults.cardColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = { onRouteSelected(route) }) {
                        Icon(imageVector = route.icon, contentDescription = route.title)
                    }
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = route.title,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        else
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onRouteSelected(route) },
                colors = CardDefaults.cardColors().copy(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = { onRouteSelected(route) }) {
                        Icon(imageVector = route.icon, contentDescription = route.title)
                    }
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = route.title,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
    }
}

@Composable
fun DrawerOptions(expanded: Boolean) {
    Card(
        onClick = {},
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.List, contentDescription = "Preferencias")
            if (expanded)
                Text(text = "Preferencias", style = MaterialTheme.typography.bodyMedium)
        }
    }
    Card(
        onClick = {},
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = Icons.Filled.Settings, contentDescription = "Configuração")
            if (expanded)
                Text(text = "Configuração", style = MaterialTheme.typography.bodyMedium)
        }
    }
}