package com.example.bakery.auth.domain

import kotlinx.coroutines.delay

enum class UsernameError { Empty, TooShort, InvalidChars }
enum class PasswordError { Empty, TooShort, NoDigit, NoLetter }
interface AuthRepository {
    suspend fun signIn(username: String, password: String): Result<Unit>
}


class FakeAuthRepository : AuthRepository {
    override suspend fun signIn(username: String, password: String): Result<Unit> {
        delay(800)
        return Result.success(Unit)
    }
}