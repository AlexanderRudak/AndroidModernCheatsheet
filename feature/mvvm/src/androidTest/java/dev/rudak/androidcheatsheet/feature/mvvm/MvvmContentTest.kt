package dev.rudak.androidcheatsheet.feature.mvvm

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.model.ProductUiModel
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.screen.MvvmContent
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.state.MvvmUiState
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test

class MvvmContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun contentState_showsProductTitle() {
        composeRule.setContent {
            MvvmContent(
                uiState = MvvmUiState.Content(
                    products = listOf(
                        ProductUiModel(
                            id = 1L,
                            title = "Test phone",
                            description = "Test description",
                            priceText = "$999.0",
                            isFavorite = false,
                        )
                    ),
                    showOnlyFavorites = false,
                ),
                onRetryClick = {},
                onErrorClick = {},
                onProductClick = {},
                onShowOnlyFavoritesChange = {},
                onFavoriteClick = {},
            )
        }

        composeRule
            .onNodeWithText("MVVM example")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Test phone")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Test description")
            .assertIsDisplayed()
    }

    @Test
    fun clickFavoriteButton() {
        var clickedProductId: Long? = null

        composeRule.setContent {
            MvvmContent(
                uiState = MvvmUiState.Content(
                    products = listOf(
                        ProductUiModel(
                            id = 1L,
                            title = "Phone 1",
                            description = "Description 1",
                            priceText = "$999",
                            isFavorite = false,
                        ),
                        ProductUiModel(
                            id = 2L,
                            title = "Phone 2",
                            description = "Description 2",
                            priceText = "$799",
                            isFavorite = false,
                        ),
                    ),
                    showOnlyFavorites = false,
                ),
                onRetryClick = {},
                onErrorClick = {},
                onProductClick = {},
                onShowOnlyFavoritesChange = {},
                onFavoriteClick = { productId ->
                    clickedProductId = productId
                },
            )
        }

        composeRule
            .onNodeWithTag("favorite_button_2")
            .performClick()

        assertEquals(2L, clickedProductId)
    }
}