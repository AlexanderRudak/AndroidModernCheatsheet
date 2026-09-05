package dev.rudak.androidcheatsheet.data.repository.remote

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: Long,
    val title: String,
    val description: String,
    val price: Double,
)