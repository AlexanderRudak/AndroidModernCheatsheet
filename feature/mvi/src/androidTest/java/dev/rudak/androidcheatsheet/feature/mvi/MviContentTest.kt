package dev.rudak.androidcheatsheet.feature.mvi.presentation.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import dev.rudak.androidcheatsheet.core.ui.model.ProductCardUiModel
import dev.rudak.androidcheatsheet.feature.mvi.presentation.event.MviEvent
import dev.rudak.androidcheatsheet.feature.mvi.presentation.state.MviState
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MviContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val product = ProductCardUiModel(
        id = 1L,
        title = "Test product",
        description = "Test description",
        priceText = "100 Kč",
        isFavorite = false,
    )

    @Test
    fun displaysPagedProduct() {
        // Проверяем, что товар из Paging отображается на экране.
        composeRule.setContent {
            val products = flowOf(PagingData.from(listOf(product))).collectAsLazyPagingItems()
            MviContent(
                state = MviState(),
                pagedProducts = products,
                onEvent = {},
            )
        }

        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText("Test product").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Test product").assertIsDisplayed()
    }

    @Test
    fun favoriteClick_sendsCorrectEvent() {
        // Проверяем передачу ID и текущего статуса избранного в MVI Event.
        var receivedEvent: MviEvent? = null

        composeRule.setContent {
            val products = flowOf(PagingData.from(listOf(product))).collectAsLazyPagingItems()
            MviContent(
                state = MviState(),
                pagedProducts = products,
                onEvent = { receivedEvent = it },
            )
        }

        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithTag("favorite_button_1").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("favorite_button_1").performClick()

        assertTrue(receivedEvent is MviEvent.FavoriteClicked)
        val event = receivedEvent as MviEvent.FavoriteClicked
        assertEquals(1L, event.productId)
        assertEquals(false, event.isFavorite)
    }
}
