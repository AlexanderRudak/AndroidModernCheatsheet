package dev.rudak.androidcheatsheet.feature.mvi.presentation.state

import dev.rudak.androidcheatsheet.core.ui.model.ProductCardUiModel
import dev.rudak.androidcheatsheet.feature.mvi.presentation.model.ProductUiModel

//data class MviState(
//    val isLoading: Boolean = false,
//    val products: List<ProductCardUiModel> = emptyList(),
//    val showOnlyFavorites: Boolean = false,
//    val errorMessage: String? = null,
//)

data class MviState(
    val products: List<ProductCardUiModel> = emptyList(),
    val showOnlyFavorites: Boolean = false,
    val currentPage: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
