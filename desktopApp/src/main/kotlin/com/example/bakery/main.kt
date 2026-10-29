package com.example.bakery

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.bakery.di.initKoin
import com.example.bakery.presentation.App

fun main() {
    initKoin()
    application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Bakery",
    ) {
        App()
    }
}

}