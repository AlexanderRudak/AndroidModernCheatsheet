package dev.rudak.androidcheatsheet.core.ui.model

data class ProductCardUiModel(
    val id: Long,
    val title: String,
    val description: String,
    val priceText: String,
    val isFavorite: Boolean,
)
