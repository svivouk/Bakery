package com.example.bakery.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bakery.onboarding.domain.onboarding.complete.isOnboardingCompleted
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface StartDestination{
    data object Loading : StartDestination
    data object Onboarding : StartDestination
    data object Login : StartDestination
}

data class MainState(val start: StartDestination = StartDestination.Loading)

class MainViewModel internal constructor(
    private val isOnboardingCompleted: isOnboardingCompleted,
): ViewModel(){
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()
    init {
        viewModelScope.launch {
            val done = isOnboardingCompleted()
            _state.update {
                it.copy(start = if (done) StartDestination.Login else StartDestination.Onboarding)
            }
        }
    }
}