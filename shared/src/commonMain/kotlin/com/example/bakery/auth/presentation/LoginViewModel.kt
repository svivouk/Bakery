package com.example.bakery.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bakery.auth.domain.SignInUseCase
import com.example.bakery.auth.domain.ValidatePasswordUseCase
import com.example.bakery.auth.domain.ValidateUsernameUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel internal constructor(
    private val validateUsername: ValidateUsernameUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val signIn: SignInUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effect = Channel<LoginEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.UsernameChanged -> _state.update {
                it.copy(username = intent.value, usernameError = null, authError = false)
            }
            is LoginIntent.PasswordChanged -> _state.update {
                it.copy(password = intent.value, passwordError = null, authError = false)
            }
            LoginIntent.TogglePasswordVisibility -> _state.update {
                it.copy(isPasswordVisible = !it.isPasswordVisible)
            }
            LoginIntent.SignInClicked -> submit()
        }
    }

    private fun submit() {
        val current = _state.value
        if (current.isLoading) return

        val usernameError = validateUsername(current.username.trim())
        val passwordError = validatePassword(current.password)
        if (usernameError != null || passwordError != null) {
            _state.update { it.copy(usernameError = usernameError, passwordError = passwordError) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, authError = false) }
            signIn(current.username.trim(), current.password)
                .onSuccess { _effect.send(LoginEffect.NavigateToHome) }
                .onFailure { _state.update { s -> s.copy(authError = true) } }
            _state.update { it.copy(isLoading = false) }
        }
    }
}