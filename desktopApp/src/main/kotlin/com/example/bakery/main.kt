package com.example.bakery

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.bakery.presentation.ui.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Bakery",
    ) {
        App()
    }
}