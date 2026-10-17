package dev.rudak.androidcheatsheet.feature.mvvm

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.rudak.androidcheatsheet.core.ui.model.ProductCardUiModel
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.screen.MvvmContent
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.state.MvvmUiState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MvvmContentTest {

    // Правило создаёт Compose-окружение для инструментальных UI-тестов.
    @get:Rule
    val composeRule = createComposeRule()

    // Проверяем, что экран отображает заголовок и данные товара из UiState.
    @Test
    fun contentState_showsProductTitle() {
        composeRule.setContent {
            MvvmContent(
                uiState = MvvmUiState(
                    products = listOf(
                        ProductCardUiModel(
                            id = 1L,
                            title = "Test phone",
                            description = "Test description",
                            priceText = "$999.0",
                            isFavorite = false,
                        ),
                    ),
                    showOnlyFavorites = false,
                ),
                onRetryClick = {},
                onLoadNextPage = {},
                onErrorClick = {},
                onProductClick = {},
                onShowOnlyFavoritesChange = {},
                onFavoriteClick = {},
            )
        }

        composeRule.onNodeWithText("MVVM example").assertIsDisplayed()
        composeRule.onNodeWithText("Test phone").assertIsDisplayed()
        composeRule.onNodeWithText("Test description").assertIsDisplayed()
    }

    // Проверяем, что нажатие на избранное передаёт ID нужного товара.
    @Test
    fun clickFavoriteButton() {
        var clickedProductId: Long? = null

        composeRule.setContent {
            MvvmContent(
                uiState = MvvmUiState(
                    products = listOf(
                        ProductCardUiModel(
                            id = 1L,
                            title = "Phone 1",
                            description = "Description 1",
                            priceText = "$999",
                            isFavorite = false,
                        ),
                        ProductCardUiModel(
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
                onLoadNextPage = {},
                onErrorClick = {},
                onProductClick = {},
                onShowOnlyFavoritesChange = {},
                onFavoriteClick = { productId ->
                    clickedProductId = productId
                },
            )
        }

        composeRule.onNodeWithTag("favorite_button_2").performClick()
        assertEquals(2L, clickedProductId)
    }

    // Проверяем фильтрацию: при включённом режиме избранного
    // отображаются только товары с isFavorite = true.
    @Test
    fun showOnlyFavorites_filtersProducts() {
        composeRule.setContent {
            MvvmContent(
                uiState = MvvmUiState(
                    products = listOf(
                        ProductCardUiModel(
                            id = 1L,
                            title = "Favorite phone",
                            description = "Favorite description",
                            priceText = "$999",
                            isFavorite = true,
                        ),
                        ProductCardUiModel(
                            id = 2L,
                            title = "Regular phone",
                            description = "Regular description",
                            priceText = "$799",
                            isFavorite = false,
                        ),
                    ),
                    showOnlyFavorites = true,
                ),
                onRetryClick = {},
                onLoadNextPage = {},
                onErrorClick = {},
                onProductClick = {},
                onShowOnlyFavoritesChange = {},
                onFavoriteClick = {},
            )
        }

        // Избранный товар виден, обычный товар отсутствует в дереве UI.
        composeRule.onNodeWithText("Favorite phone").assertIsDisplayed()
        composeRule.onNodeWithText("Regular phone").assertDoesNotExist()
    }

}