package dev.rudak.androidcheatsheet.domain.model.shop

data class ProductDetailsData(
    val product: Product?,
    val recommendedProducts: List<Product>,
)