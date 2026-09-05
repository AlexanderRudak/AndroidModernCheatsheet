package dev.rudak.androidcheatsheet.feature.mvvm.presentation.mapper

import dev.rudak.androidcheatsheet.core.ui.model.ProductCardUiModel
import dev.rudak.androidcheatsheet.domain.model.shop.Product
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.model.ProductUiModel

fun Product.toUiModel(): ProductCardUiModel {
    return ProductCardUiModel(
        id = id,
        title = title,
        description = description,
        priceText = "$$price",
        isFavorite = isFavorite,
    )
}