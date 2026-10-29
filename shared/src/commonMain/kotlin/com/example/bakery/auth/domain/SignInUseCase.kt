package com.example.bakery.auth.domain

class SignInUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String) =
        repository.signIn(username, password)
}