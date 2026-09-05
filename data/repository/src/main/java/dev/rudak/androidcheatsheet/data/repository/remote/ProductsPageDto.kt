package dev.rudak.androidcheatsheet.data.repository.remote

data class ProductsPageDto(
    val products: List<ProductDto>,
    val currentPage: Int,
    val totalPages: Int,
)
