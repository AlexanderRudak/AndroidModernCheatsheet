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
            MviCommand.LoadProducts(
                page = 1,
                pageSize = 10,
            ),
            result.command,
        )
    }
}