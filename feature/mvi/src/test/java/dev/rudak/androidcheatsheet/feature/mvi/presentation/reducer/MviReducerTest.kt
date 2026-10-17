package dev.rudak.androidcheatsheet.feature.mvi.presentation.reducer

import dev.rudak.androidcheatsheet.feature.mvi.presentation.command.MviCommand
import dev.rudak.androidcheatsheet.feature.mvi.presentation.event.MviEvent
import dev.rudak.androidcheatsheet.feature.mvi.presentation.state.MviState
import org.junit.Assert
import org.junit.Test

class MviReducerTest {

    private val reducer = MviReducer()

    @Test
    fun `GIVEN initial state WHEN LoadProducts THEN return loading state and LoadProducts command`() {
        val initialState = MviState(
            isLoading = false,
            errorMessage = "Previous error",
        )

        val result = reducer.reduce(
            state = initialState,
            event = MviEvent.LoadProducts(
                page = 1,
            ),
        )

        Assert.assertTrue(result.state.isLoading)
        Assert.assertNull(result.state.errorMessage)
        Assert.assertEquals(
            listOf(
                MviCommand.LoadProducts(
                    page = 1,
                    pageSize = 10,
                )
            ),
            result.commands,
        )
    }

    @Test
    fun `GIVEN state WHEN FavoriteClicked THEN return unchanged state and action with analytic command`() {
        val initialState = MviState()

        val result = reducer.reduce(
            state = initialState,
            event = MviEvent.FavoriteClicked(
                productId = 5L,
                isFavorite = false,
            ),
        )

        Assert.assertEquals(initialState, result.state)
        Assert.assertEquals(
            listOf(
                MviCommand.ToggleFavorite(productId = 5L),
                MviCommand.Analytic.FavoriteClicked(
                    productId = 5L,
                    isFavorite = false,
                ),
            ),
            result.commands,
        )
    }

    @Test
    fun `GIVEN state WHEN ProductClicked THEN return unchanged state and analytic command`() {
        val initialState = MviState()

        val result = reducer.reduce(
            state = initialState,
            event = MviEvent.ProductClicked(productId = 7L),
        )

        Assert.assertEquals(initialState, result.state)
        Assert.assertEquals(
            listOf(MviCommand.Analytic.ProductClicked(productId = 7L)),
            result.commands,
        )
    }
}
