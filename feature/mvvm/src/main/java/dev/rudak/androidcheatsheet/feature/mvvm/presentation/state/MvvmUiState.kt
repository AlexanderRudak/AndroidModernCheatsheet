package dev.rudak.androidcheatsheet.feature.mvvm.presentation.state

import dev.rudak.androidcheatsheet.core.ui.model.ProductCardUiModel
import dev.rudak.androidcheatsheet.domain.model.shop.Product
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.model.ProductUiModel

//sealed interface MvvmUiState {
//
//    data object Loading : MvvmUiState
//
//    data class Content(
//        val products: List<ProductCardUiModel>,
//        val showOnlyFavorites: Boolean,
//    ) : MvvmUiState
//
//    data class Error(
//        val message: String,
//    ) : MvvmUiState
//}

data class MvvmUiState(
    val products: List<ProductCardUiModel> = emptyList(),
    val showOnlyFavorites: Boolean = false,
    val currentPage: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)