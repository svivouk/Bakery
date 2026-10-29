package com.example.bakery.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.bakery.auth.presentation.LoginScreen
import com.example.bakery.home.presentation.HomeScreen
import com.example.bakery.onboarding.presentation.OnboardingScreen
import kotlinx.serialization.Serializable

@Composable
fun AppNavigation(start: NavKey) {
    val backStack = remember { mutableStateListOf<NavKey>(start) }

    fun replaceAllWith(route: NavKey) {
        backStack.clear()
        backStack.add(route)
    }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<OnboardingRoute> {
                OnboardingScreen(onFinished = { replaceAllWith(LoginRoute) })
            }
            entry<LoginRoute> {
                LoginScreen(onSignedIn = { replaceAllWith(HomeRoute) })
            }
            entry<HomeRoute> { HomeScreen() }
        },
    )
}
