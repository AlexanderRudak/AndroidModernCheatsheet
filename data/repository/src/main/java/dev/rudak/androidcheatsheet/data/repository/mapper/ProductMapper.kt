package dev.rudak.androidcheatsheet.data.repository.mapper

import dev.rudak.androidcheatsheet.data.repository.remote.ProductDto
import dev.rudak.androidcheatsheet.domain.model.shop.Category
import dev.rudak.androidcheatsheet.domain.model.shop.Product

fun ProductDto.toDomain(
    isFavorite: Boolean,
): Product {
    return Product(
        id = id,
        title = title,
        description = description,
        price = price,
        isFavorite = isFavorite,
    )
}