package dev.rudak.androidcheatsheet.feature.mvi.core

import kotlinx.coroutines.flow.Flow

interface MviActor<Command, Event> {

    fun process(
        commands: Flow<Command>,
    ): Flow<Event>
}