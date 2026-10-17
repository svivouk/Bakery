package com.example.bakery.presentation.ui
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import bakery.shared.generated.resources.Res
import bakery.shared.generated.resources.home_placeholder
import com.example.bakery.AboutApp.AboutScreen
import com.example.bakery.data.CatalogApi
import com.example.bakery.presentation.ui.theme.AppTheme
import com.example.bakery.presentation.ui.theme.primaryContainerLight
import org.jetbrains.compose.resources.stringResource

const val DEFAULT_API_BASE_URL = "http://localhost:5065"
@Composable
@Preview
fun App(apiBaseUrl: String = DEFAULT_API_BASE_URL) {
    AppTheme {
        Column(
            modifier = Modifier
                .background(primaryContainerLight)
                .systemBarsPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val api = remember(apiBaseUrl) { CatalogApi(apiBaseUrl) }
            DisposableEffect(api) { onDispose { api.close() } }
            var AboutDone by rememberSaveable { mutableStateOf(false) }

            if (!AboutDone) {
                AboutScreen(
                    api = api,
                    onFinished = { AboutDone = true }
                )
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(Res.string.home_placeholder))
                }
            }
        }
    }
}
/*
@Composable
private fun FirstTimeWelcome(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryContainerLightHighContrast)
            .clickable {

            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = ("Welcome to Bakery"),
            color = secondaryContainerLight,
            fontFamily = displayFontFamily,
            fontSize = 12.em,
            lineHeight = 1.em,
            textAlign = TextAlign.Center
        )
    }
}*/