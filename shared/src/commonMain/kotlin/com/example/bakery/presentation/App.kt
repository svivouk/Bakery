package com.example.bakery.presentation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bakery.main.MainViewModel
import com.example.bakery.main.StartDestination
import com.example.bakery.onboarding.domain.onboarding.complete.isOnboardingCompleted
import com.example.bakery.presentation.ui.theme.AppTheme
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

const val DEFAULT_API_BASE_URL = "http://localhost:5065"
@Composable
@Preview
fun App() = KoinContext {
    AppTheme {
        val vm = koinViewModel<MainViewModel>()
        val state by vm.state.collectAsState()

        when (state.start) {
            StartDestination.Loading ->
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            StartDestination.Onboarding -> AppNavigation(start = OnboardingRoute)
            StartDestination.Login -> AppNavigation(start = LoginRoute)
        }
    }
}
