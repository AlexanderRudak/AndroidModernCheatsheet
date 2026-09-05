package dev.rudak.androidcheatsheet.feature.mvi.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import dev.rudak.androidcheatsheet.domain.usecase.shop.GetPagedProductsUseCase
import dev.rudak.androidcheatsheet.feature.mvi.core.BaseMviViewModel
import dev.rudak.androidcheatsheet.feature.mvi.core.ReduceResult
import dev.rudak.androidcheatsheet.feature.mvi.presentation.actor.MviActor
import dev.rudak.androidcheatsheet.feature.mvi.presentation.command.MviCommand
import dev.rudak.androidcheatsheet.feature.mvi.presentation.event.MviEvent
import dev.rudak.androidcheatsheet.feature.mvi.presentation.mapper.toUiModel
import dev.rudak.androidcheatsheet.feature.mvi.presentation.news.MviNews
import dev.rudak.androidcheatsheet.feature.mvi.presentation.reducer.MviReducer
import dev.rudak.androidcheatsheet.feature.mvi.presentation.state.MviState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MviViewModel(
    private val reducer: MviReducer,
    private val actor: MviActor,
    private val getPagedProductsUseCase: GetPagedProductsUseCase,
) : BaseMviViewModel<MviState, MviEvent, MviCommand, MviNews>(
    initialState = MviState(),
) {

    init {
        onEvent(MviEvent.LoadProducts(page = 1))
    }

    val pagedProducts =
        getPagedProductsUseCase()
            .map { pagingData ->
                pagingData.map { product ->
                    product.toUiModel()
                }
            }
            .cachedIn(viewModelScope)

    override fun reduce(
        state: MviState,
        event: MviEvent,
    ): ReduceResult<MviState, MviCommand> {
        return reducer.reduce(
            state = state,
            event = event,
        )
    }

    override suspend fun execute(
        command: MviCommand,
    ): MviEvent {
        return actor.execute(command)
    }

    override fun handleEvent(event: MviEvent) {
        when (event) {
            is MviEvent.ProductClicked -> {
                sendNews(
                    MviNews.NavigateToDetails(event.productId)
                )
            }

            else -> Unit
        }
    }
}

//class MviViewModel2(
//    private val reducer: MviReducer,
//    private val actor: MviActor,
//) : ViewModel() {
//
//    private val _state = MutableStateFlow(MviState())
//    val state: StateFlow<MviState> = _state.asStateFlow()
//
//    private val _news = MutableSharedFlow<MviNews>()
//    val news = _news.asSharedFlow()
//
//    init {
//        onEvent(MviEvent.LoadProducts(page = 1))
//    }
//
//    fun onEvent(event: MviEvent) {
//        val reduceResult = reducer.reduce(
//            state = _state.value,
//            event = event,
//        )
//
//        _state.value = reduceResult.state
//
//        when (event) {
//            is MviEvent.ProductClicked -> {
//                viewModelScope.launch {
//                    _news.emit(
//                        MviNews.NavigateToDetails(event.productId)
//                    )
//                }
//            }
//
//            else -> Unit
//        }
//
//        val command = reduceResult.command ?: return
//
//        viewModelScope.launch {
//            val resultEvent = actor.execute(command)
//            onEvent(resultEvent)
//        }
//    }
//}