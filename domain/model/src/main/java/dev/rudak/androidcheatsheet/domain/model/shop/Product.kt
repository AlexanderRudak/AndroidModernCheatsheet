package dev.rudak.androidcheatsheet.domain.model.shop

data class Product(
    val id: Long,
    val title: String,
    val description: String,
    val price: Double,
    //val category: Category,
    val isFavorite: Boolean,
)
