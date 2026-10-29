package com.example.bakery.auth.domain

class ValidatePasswordUseCase {
    operator fun invoke(value: String): PasswordError? = when {
        value.isEmpty() -> PasswordError.Empty
        value.length < 6 -> PasswordError.TooShort
        value.none { it.isLetter() } -> PasswordError.NoLetter
        value.none { it.isDigit() } -> PasswordError.NoDigit
        else -> null
    }
}