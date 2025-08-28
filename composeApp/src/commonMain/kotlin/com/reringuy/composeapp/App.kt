package com.reringuy.composeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
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
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.reringuy.shared.routes.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val bottomBarRoutes = listOf(Routes.Menu, Routes.Schedule, Routes.Card, Routes.Profile)
    val windowSize = currentWindowAdaptiveInfo().windowSizeClass
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val showBottomBar =
        windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)

    var selectedDestination: Routes by remember { mutableStateOf(Routes.Menu) }

    if (!showBottomBar)
        Scaffold(
            modifier = Modifier.safeDrawingPadding(),
            topBar = { CommonTopBar(scrollBehavior) },
            bottomBar = {
                PontoBottomAppBar(
                    menuRoutes = bottomBarRoutes,
                    selectedDestination
                ) { selectedDestination = it }
            }
        ) { padding ->
            BoxWithConstraints(modifier = Modifier.padding(padding)) {
                Text(text = "aaaaaaaaaaa", style = MaterialTheme.typography.titleLarge)
            }
        }
    else {
        if (windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)) {
            PermanentNavigationDrawer(
                modifier = Modifier.safeDrawingPadding(),
                drawerContent = {
                    Column(
                        modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(modifier = Modifier.fillMaxHeight().padding(8.dp, 16.dp)) {
                            NavigationButtons(
                                bottomBarRoutes,
                                selectedDestination
                            ) { selectedDestination = it }
                        }

                    }
                }
            ) {
                Scaffold(topBar = { CommonTopBar(scrollBehavior) }) { paddingValues ->
                    BoxWithConstraints(modifier = Modifier.padding(paddingValues)) {
                        Text(text = "aaaaaaaaaaa", style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
        } else {
            ModalNavigationDrawer(
                modifier = Modifier.safeDrawingPadding(),
                drawerContent = {
                    Column(
                        modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(modifier = Modifier.fillMaxHeight().padding(8.dp, 16.dp)) {
                            NavigationButtons(
                                bottomBarRoutes,
                                selectedDestination
                            ) { selectedDestination = it }
                        }
                    }
                }
            ) {
                Scaffold(topBar = { CommonTopBar(scrollBehavior) }) { paddingValues ->
                    BoxWithConstraints(modifier = Modifier.padding(paddingValues)) {
                        Text(text = "aaaaaaaaaaa", style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopBar(scrollBehavior: TopAppBarScrollBehavior) {
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

@Composable
fun PontoBottomAppBar(
    menuRoutes: List<Routes>,
    selectedRoute: Routes,
    onRouteSelected: (Routes) -> Unit,
) {
    BottomAppBar(containerColor = MaterialTheme.colorScheme.primaryContainer) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            NavigationButtons(menuRoutes, selectedRoute, onRouteSelected)
        }
    }
}

@Composable
fun NavigationButtons(
    menuRoutes: List<Routes>,
    selectedRoute: Routes,
    onRouteSelected: (Routes) -> Unit,
) {
    menuRoutes.forEach { route ->
        if (route == selectedRoute)
            Card(
                colors = CardDefaults.cardColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                IconButton(onClick = { }) {
                    when (route) {
                        Routes.Card -> Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = route.title
                        )

                        Routes.Menu -> Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = route.title
                        )

                        Routes.Profile -> Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = route.title
                        )

                        Routes.Schedule -> Icon(
                            imageVector = Icons.Rounded.Info,
                            contentDescription = route.title
                        )
                    }
                }
            }
        else
            IconButton(onClick = { onRouteSelected(route) }) {
                when (route) {
                    Routes.Card -> Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = route.title
                    )

                    Routes.Menu -> Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = route.title
                    )

                    Routes.Profile -> Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = route.title
                    )

                    Routes.Schedule -> Icon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = route.title
                    )
                }
            }
    }
}