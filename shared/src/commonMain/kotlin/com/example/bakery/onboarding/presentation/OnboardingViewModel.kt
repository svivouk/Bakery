package com.example.bakery.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bakery.onboarding.data.CatalogApi
import com.example.bakery.onboarding.domain.onboarding.save.SaveOnboardingCompletedUseCase
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class OnboardingViewModel internal constructor(
    private val saveOnboardingCompletedUseCase: SaveOnboardingCompletedUseCase,
    private val api: CatalogApi,
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    private val _effect = Channel<OnboardingEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        loadPopular()
    }

    fun onIntent(intent: OnboardingIntent) {
        when (intent) {
            is OnboardingIntent.PageChanged ->
                _state.update { it.copy(currentPage = intent.index) }

            OnboardingIntent.SkipClicked -> finish()

            OnboardingIntent.NextClicked -> {
                if (_state.value.isLastPage) finish()
                else _state.update { it.copy(currentPage = it.currentPage + 1) }
            }

            OnboardingIntent.RetryClicked -> loadPopular()
        }
    }

    private fun loadPopular() = viewModelScope.launch {
        _state.update { it.copy(popular = ProductsState.Loading) }
        val result = try {
            ProductsState.Loaded(api.products(pageSize = 5))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            println("API Error (${e::class.simpleName}): ${e.message}")
            ProductsState.Failed
        }
        _state.update { it.copy(popular = result) }
    }

    private fun finish() = viewModelScope.launch {
        _state.update { it.copy(isSaving = true) }
        saveOnboardingCompletedUseCase()
        _state.update { it.copy(isSaving = false) }
        _effect.send(OnboardingEffect.NavigateToHome)
    }
}