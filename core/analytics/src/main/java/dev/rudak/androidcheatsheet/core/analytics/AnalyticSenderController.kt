package dev.rudak.androidcheatsheet.core.analytics

interface AnalyticSenderController {

    fun sendEvent(
        event: AnalyticEvent,
    )
}