package dev.rudak.androidcheatsheet.feature.mvvm.presentation.model

data class ProductUiModel(
    val id: Long,
    val title: String,
    val description: String,
    val priceText: String,
    val isFavorite: Boolean,
)