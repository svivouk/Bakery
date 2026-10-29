package com.example.bakery.onboarding.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bakery.shared.generated.resources.Res
import bakery.shared.generated.resources.about_next
import bakery.shared.generated.resources.about_skip
import bakery.shared.generated.resources.about_start
import com.example.bakery.onboarding.data.ProductListItem
import com.example.bakery.presentation.ui.theme.primaryContainerLight
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import bakery.shared.generated.resources.popular_empty
import bakery.shared.generated.resources.popular_error
import bakery.shared.generated.resources.popular_retry
import bakery.shared.generated.resources.price_format
import bakery.shared.generated.resources.reviews_count
import com.example.bakery.presentation.ui.theme.errorDark
import com.example.bakery.presentation.ui.theme.onSurfaceDark
import com.example.bakery.presentation.ui.theme.onSurfaceLight
import com.example.bakery.presentation.ui.theme.outlineVariantLight
import com.example.bakery.presentation.ui.theme.primaryLight
import org.jetbrains.compose.resources.pluralStringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToLong


@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { state.pages.size })

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                OnboardingEffect.NavigateToHome -> onFinished()
            }
        }
    }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { index ->
            viewModel.onIntent(OnboardingIntent.PageChanged(index))
        }
    }

    val isLast = state.isLastPage

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryContainerLight)
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            TextButton(
                onClick = { viewModel.onIntent(OnboardingIntent.SkipClicked) },
                enabled = !isLast && !state.isSaving,
                modifier = Modifier.alpha(if (isLast) 0f else 1f),
            ) {
                Text(stringResource(Res.string.about_skip))
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) { index ->
            val page = state.pages[index]
            PageContent(page) {
                if (page.showsApiData) {
                    PopularProducts(state = state.popular, onRetry = { viewModel.onIntent(
                        OnboardingIntent.RetryClicked) })
                }
            }
        }

        PagerIndicator(
            pageCount = state.pages.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.CenterHorizontally),
        )

        Button(
            onClick = {
                if (isLast) {
                    viewModel.onIntent(OnboardingIntent.NextClicked)
                } else {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                }
            },
            enabled = !state.isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp),
        ) {
            Text(
                text = stringResource(if (isLast) Res.string.about_start else Res.string.about_next),
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 4.dp),
            )
        }
    }
}

@Composable
private fun PageContent(page: OnboardingPage, extra: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(page.title),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 24.dp),
        )
        Text(
            text = stringResource(page.text),
            style = MaterialTheme.typography.bodyMedium,
            color = onSurfaceLight,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 12.dp, bottom = 16.dp),
        )
        extra()
    }
}

@Composable
private fun PagerIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    ) {
    Row(modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(pageCount) { index ->
            val selected = index == currentPage
            val color by animateColorAsState(
                if (selected) primaryLight else outlineVariantLight,
            )
            Box(Modifier
                .height(8.dp)
                .width(8.dp)
                .background(color,CircleShape))
        }
    }
}

@Composable
private fun PopularProducts(state: ProductsState, onRetry: () -> Unit) {
    when (state) {
        ProductsState.Loading -> CircularProgressIndicator()
        ProductsState.Failed -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(Res.string.popular_error),
                color = errorDark,
                textAlign = TextAlign.Center,
            )
            TextButton(onClick = onRetry) { Text(stringResource(Res.string.popular_retry)) }
        }
        is ProductsState.Loaded ->
            if (state.items.isEmpty()) {
                Text(stringResource(Res.string.popular_empty), textAlign = TextAlign.Center)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.items.forEach { ProductRow(it) }
                }
            }
    }
    }



@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ProductRow(product: ProductListItem) {
    Card(modifier = Modifier
        .fillMaxWidth(),
        ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "★ ${formatRating(product.avgRating)} · " +
                            pluralStringResource(Res.plurals.reviews_count,
                                product.reviewCount,
                                product.reviewCount),
                    style = MaterialTheme.typography.bodySmall,
                    color = onSurfaceDark,
                )
            }
            Text(
                text = stringResource(Res.string.price_format, formatMoney(product.basePrice)),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 12.dp),
            )
        }
    }
}
private fun formatMoney(value: Double): String {
    val cents = (value * 100).roundToLong()
    return "${cents / 100}.${(cents % 100).toString().padStart(2, '0')}"
}
private fun formatRating(value: Double): String = ((value * 10).roundToInt() / 10.0).toString()
