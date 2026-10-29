package com.example.bakery.auth.domain

class ValidateUsernameUseCase {
    operator fun invoke(value: String): UsernameError? = when {
        value.isBlank() -> UsernameError.Empty
        value.length < 3 -> UsernameError.TooShort
        !value.all { it.isLetterOrDigit() || it == '_' || it == '.' } -> UsernameError.InvalidChars
        else -> null
    }
}