package dev.rudak.androidcheatsheet.domain.model.shop

data class CartItem(
    val product: Product,
    val quantity: Int,
)