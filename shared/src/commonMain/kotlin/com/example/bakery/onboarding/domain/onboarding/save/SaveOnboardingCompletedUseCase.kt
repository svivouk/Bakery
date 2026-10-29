package com.example.bakery.onboarding.domain.onboarding.save

import com.example.bakery.onboarding.domain.onboarding.OnboardingRepository

class SaveOnboardingCompletedUseCase (private val repository: OnboardingRepository){
    suspend operator fun invoke(completed: Boolean = true){
        repository.setOnboardingComplete(true)
    }
}