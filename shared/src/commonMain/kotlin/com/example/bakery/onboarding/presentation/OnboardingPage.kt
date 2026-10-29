package com.example.bakery.onboarding.presentation

import bakery.shared.generated.resources.Res
import bakery.shared.generated.resources.about_1_text
import bakery.shared.generated.resources.about_1_title
import bakery.shared.generated.resources.about_2_text
import bakery.shared.generated.resources.about_2_title
import bakery.shared.generated.resources.about_3_text
import bakery.shared.generated.resources.about_3_title
import bakery.shared.generated.resources.about_4_text
import bakery.shared.generated.resources.about_4_title
import com.example.bakery.onboarding.data.ProductListItem

import org.jetbrains.compose.resources.StringResource
data class OnboardingState(
    val pages: List<OnboardingPage> = onboardingPages,
    val currentPage: Int = 0,
    val isSaving: Boolean = false,
    val popular: ProductsState = ProductsState.Loading,
){
    val isLastPage get() = currentPage == pages.lastIndex
}
sealed interface OnboardingIntent {
    data object NextClicked : OnboardingIntent
    data object SkipClicked : OnboardingIntent
    data object RetryClicked : OnboardingIntent
    data class PageChanged(val index: Int) : OnboardingIntent
}
sealed interface OnboardingEffect {
    data object NavigateToHome : OnboardingEffect
}
data class OnboardingPage(
    val title: StringResource,
    val text: StringResource,
    val showsApiData: Boolean = false,
)

val onboardingPages = listOf(
    OnboardingPage(Res.string.about_1_title, Res.string.about_1_text),
    OnboardingPage(Res.string.about_2_title, Res.string.about_2_text),
    OnboardingPage(Res.string.about_3_title, Res.string.about_3_text, showsApiData = true),
    OnboardingPage(Res.string.about_4_title, Res.string.about_4_text),
)

sealed interface ProductsState {
    data object Loading : ProductsState
    data class Loaded(val items: List<ProductListItem>) : ProductsState
    data object Failed : ProductsState
}
