package dev.rudak.androidcheatsheet.feature.mvi.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

abstract class BaseMviViewModelOld<State, Event, Command, News>(
    initialState: State,
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _news = MutableSharedFlow<News>()
    val news: SharedFlow<News> = _news.asSharedFlow()

    fun onEvent(event: Event) {
        val reduceResult = reduce(
            state = _state.value,
            event = event,
        )

        _state.value = reduceResult.state

        handleEvent(event)

        reduceResult.commands.forEach { command ->
            viewModelScope.launch {
                val resultEvent = execute(command)
                onEvent(resultEvent)
            }
        }
    }

    protected abstract fun reduce(
        state: State,
        event: Event,
    ): ReduceResult<State, Command>

    protected abstract suspend fun execute(
        command: Command,
    ): Event

    protected open fun handleEvent(event: Event) = Unit

    protected fun sendNews(news: News) {
        viewModelScope.launch {
            _news.emit(news)
        }
    }
}




abstract class BaseMviViewModel<State, Event, Command, News>(
    initialState: State,
    private val actors: List<MviActor<Command, Event>>,
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _news = MutableSharedFlow<News>()
    val news: SharedFlow<News> = _news.asSharedFlow()

    fun onEvent(event: Event) {
        val reduceResult = reduce(
            state = _state.value,
            event = event,
        )

        _state.value = reduceResult.state

        handleEvent(event)

        /*viewModelScope.launch {
            process(reduceResult.commands.asFlow())
                .collect { resultEvent ->
                    onEvent(resultEvent)
                }
        }*/
        if (reduceResult.commands.isNotEmpty()) {
            viewModelScope.launch {
                actors
                    .map { actor ->
                        actor.process(reduceResult.commands.asFlow())
                    }
                    .merge()
                    .collect { resultEvent ->
                        onEvent(resultEvent)
                    }
            }
        }
    }

    protected abstract fun reduce(
        state: State,
        event: Event,
    ): ReduceResult<State, Command>

    /*protected abstract fun process(
        commands: Flow<Command>,
    ): Flow<Event>*/

    protected open fun handleEvent(event: Event) = Unit

    protected fun sendNews(news: News) {
        viewModelScope.launch {
            _news.emit(news)
        }
    }
}