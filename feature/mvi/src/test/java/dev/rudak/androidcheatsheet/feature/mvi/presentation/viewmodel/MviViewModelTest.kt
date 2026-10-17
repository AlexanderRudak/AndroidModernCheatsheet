package dev.rudak.androidcheatsheet.feature.mvi.presentation.viewmodel

import androidx.paging.PagingData
import dev.rudak.androidcheatsheet.domain.usecase.shop.GetPagedProductsUseCase
import dev.rudak.androidcheatsheet.domain.usecase.shop.ObserveFavoriteIdsUseCase
import dev.rudak.androidcheatsheet.feature.mvi.core.MviActor
import dev.rudak.androidcheatsheet.feature.mvi.presentation.command.MviCommand
import dev.rudak.androidcheatsheet.feature.mvi.presentation.event.MviEvent
import dev.rudak.androidcheatsheet.feature.mvi.presentation.news.MviNews
import dev.rudak.androidcheatsheet.feature.mvi.presentation.reducer.MviReducer
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.After
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MviViewModelTest {

    private val getPagedProductsUseCase = mockk<GetPagedProductsUseCase>()
    private val observeFavoriteIdsUseCase = mockk<ObserveFavoriteIdsUseCase>()

    private fun createViewModel(
        actors: List<MviActor<MviCommand, MviEvent>> = emptyList(),
    ): MviViewModel {
        every { getPagedProductsUseCase() } returns flowOf(PagingData.empty())
        every { observeFavoriteIdsUseCase() } returns flowOf(emptyList())

        return MviViewModel(
            reducer = MviReducer(),
            actors = actors,
            getPagedProductsUseCase = getPagedProductsUseCase,
            observeFavoriteIdsUseCase = observeFavoriteIdsUseCase,
        )
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN ViewModel created WHEN initialized THEN starts loading first page`() = runTest {
        val viewModel = createViewModel()
        runCurrent()

        assertTrue(viewModel.state.value.isLoading)
        assertEquals(null, viewModel.state.value.errorMessage)
    }

    @Test
    fun `GIVEN ProductClicked WHEN handled THEN emits NavigateToDetails news`() = runTest {
        val viewModel = createViewModel()
        val news = async(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.news.first()
        }

        viewModel.onEvent(MviEvent.ProductClicked(productId = 42L))
        runCurrent()

        assertEquals(MviNews.NavigateToDetails(42L), news.await())
    }

    @Test
    fun `GIVEN Actor emits failure WHEN initial command processed THEN updates state`() = runTest {
        val actor = object : MviActor<MviCommand, MviEvent> {
            override fun process(commands: Flow<MviCommand>): Flow<MviEvent> =
                commands
                    .filterIsInstance<MviCommand.LoadProducts>()
                    .map { MviEvent.ProductsLoadingFailed("Network error") }
        }
        val viewModel = createViewModel(actors = listOf(actor))
        runCurrent()

        assertFalse(viewModel.state.value.isLoading)
        assertEquals("Network error", viewModel.state.value.errorMessage)
    }
}
