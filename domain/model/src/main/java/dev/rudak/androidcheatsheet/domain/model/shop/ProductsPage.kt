package dev.rudak.androidcheatsheet.domain.model.shop

data class ProductsPage(
    val products: List<Product>,
    val currentPage: Int,
    val totalPages: Int,
)
