package com.example.bakery.di

import com.example.bakery.auth.domain.AuthRepository
import com.example.bakery.auth.domain.FakeAuthRepository
import com.example.bakery.auth.domain.SignInUseCase
import com.example.bakery.auth.domain.ValidatePasswordUseCase
import com.example.bakery.auth.domain.ValidateUsernameUseCase
import com.example.bakery.auth.presentation.LoginViewModel
import com.example.bakery.main.MainViewModel
import com.example.bakery.onboarding.data.CatalogApi
import org.koin.dsl.module
import com.example.bakery.onboarding.data.OnboardingRepositoryImpl
import com.example.bakery.onboarding.domain.onboarding.OnboardingRepository
import com.example.bakery.onboarding.domain.onboarding.complete.isOnboardingCompleted
import com.example.bakery.onboarding.domain.onboarding.save.SaveOnboardingCompletedUseCase
import com.example.bakery.onboarding.presentation.OnboardingViewModel
import com.example.bakery.presentation.DEFAULT_API_BASE_URL
import com.russhwolf.settings.Settings
import org.koin.core.module.dsl.viewModelOf

val appModule = module {
    single<Settings> { Settings() }
    single<OnboardingRepository> { OnboardingRepositoryImpl(get()) }
    factory { SaveOnboardingCompletedUseCase(get()) }
    factory { isOnboardingCompleted(get()) }
    single { CatalogApi(DEFAULT_API_BASE_URL) }
    factory { ValidateUsernameUseCase() }
    factory { ValidatePasswordUseCase() }
    single<AuthRepository> { FakeAuthRepository() }
    factory { SignInUseCase(get()) }

    viewModelOf(::MainViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::OnboardingViewModel)
}