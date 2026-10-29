package com.example.bakery.auth.presentation

import com.example.bakery.auth.domain.PasswordError
import com.example.bakery.auth.domain.UsernameError

data class LoginState(
    val username: String = "",
    val password: String = "",
    val usernameError: UsernameError? = null,
    val passwordError: PasswordError? = null,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val authError: Boolean = false,
)

sealed interface LoginIntent {
    data class UsernameChanged(val value: String) : LoginIntent
    data class PasswordChanged(val value: String) : LoginIntent
    data object TogglePasswordVisibility : LoginIntent
    data object SignInClicked : LoginIntent
}

sealed interface LoginEffect {
    data object NavigateToHome : LoginEffect
}