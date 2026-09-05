package dev.rudak.androidcheatsheet.feature.mvi.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseMviViewModel<State, Event, Command, News>(
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

        val command = reduceResult.command ?: return

        viewModelScope.launch {
            val resultEvent = execute(command)
            onEvent(resultEvent)
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