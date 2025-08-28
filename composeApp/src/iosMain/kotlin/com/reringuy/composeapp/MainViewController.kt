package com.reringuy.composeapp

import androidx.compose.ui.window.ComposeUIViewController

@Suppress("UNUSED")
fun MainViewController() = ComposeUIViewController(configure = { enforceStrictPlistSanityCheck = false }) { App() }
