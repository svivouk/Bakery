package com.example.bakery.onboarding.data

import com.example.bakery.onboarding.domain.onboarding.OnboardingRepository
import com.russhwolf.settings.Settings


class OnboardingRepositoryImpl(private val settings: Settings): OnboardingRepository{
    override suspend fun setOnboardingComplete(completed: Boolean) {
        settings.putBoolean(KEY_ONBOARDING_DONE, completed)
    }

    override suspend fun isOnboardingCompleted(): Boolean =
        settings.getBoolean(KEY_ONBOARDING_DONE, false)
    private companion object { const val KEY_ONBOARDING_DONE = "onboarding_done" }
}

