package dev.rudak.androidcheatsheet.core.analytics

data class AnalyticEvent(
    val name: String,
    val params: Map<String, Any> = emptyMap(),
)