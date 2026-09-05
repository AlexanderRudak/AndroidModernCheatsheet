package dev.rudak.androidcheatsheet.feature.mvi.core

import dev.rudak.androidcheatsheet.feature.mvi.presentation.command.MviCommand
import dev.rudak.androidcheatsheet.feature.mvi.presentation.state.MviState

data class ReduceResult<State, Command>(
    val state: State,
    val command: Command? = null,
)