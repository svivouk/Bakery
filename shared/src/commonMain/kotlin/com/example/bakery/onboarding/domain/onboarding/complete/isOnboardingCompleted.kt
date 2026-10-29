package com.example.bakery.onboarding.domain.onboarding.complete

import com.example.bakery.onboarding.domain.onboarding.OnboardingRepository

class isOnboardingCompleted(private val repository: OnboardingRepository) {
    suspend operator fun invoke(): Boolean = repository.isOnboardingCompleted()
}