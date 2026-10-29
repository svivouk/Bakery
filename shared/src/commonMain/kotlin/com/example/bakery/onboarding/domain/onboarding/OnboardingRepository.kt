package com.example.bakery.onboarding.domain.onboarding

interface OnboardingRepository{
    suspend fun setOnboardingComplete(completed: Boolean)
    suspend fun isOnboardingCompleted(): Boolean
}