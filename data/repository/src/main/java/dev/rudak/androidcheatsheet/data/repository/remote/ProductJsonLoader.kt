package dev.rudak.androidcheatsheet.data.repository.remote

import android.content.Context
import kotlinx.serialization.json.Json

class ProductJsonLoader(
    private val context: Context,
    private val json: Json,
) {

    fun loadProducts(): List<ProductDto> {
        val jsonString = context.assets
            .open(PRODUCTS_FILE_NAME)
            .bufferedReader()
            .use { it.readText() }

        return json.decodeFromString<List<ProductDto>>(jsonString)
    }

    private companion object {
        const val PRODUCTS_FILE_NAME = "products.json"
    }
}