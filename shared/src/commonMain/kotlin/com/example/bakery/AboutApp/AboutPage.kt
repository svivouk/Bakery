package com.example.bakery.AboutApp

import bakery.shared.generated.resources.Res
import bakery.shared.generated.resources.about_1_text
import bakery.shared.generated.resources.about_1_title
import bakery.shared.generated.resources.about_2_text
import bakery.shared.generated.resources.about_2_title
import bakery.shared.generated.resources.about_3_text
import bakery.shared.generated.resources.about_3_title
import bakery.shared.generated.resources.about_4_text
import bakery.shared.generated.resources.about_4_title
import com.example.bakery.data.ProductListItem

import org.jetbrains.compose.resources.StringResource

data class AboutPage(
    val title: StringResource,
    val text: StringResource,
    val showsApiData: Boolean = false,
)

public val pages = listOf(
    AboutPage(Res.string.about_1_title, Res.string.about_1_text),
    AboutPage(Res.string.about_2_title, Res.string.about_2_text),
    AboutPage(Res.string.about_3_title, Res.string.about_3_text, showsApiData = true),
    AboutPage(Res.string.about_4_title, Res.string.about_4_text),
)

public sealed interface PopularState {
    data object Loading : PopularState
    data class Loaded(val items: List<ProductListItem>) : PopularState
    data object Failed : PopularState
}
