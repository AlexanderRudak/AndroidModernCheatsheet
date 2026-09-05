package dev.rudak.androidcheatsheet.navigation

object Destinations {

    const val PRODUCTS_MVVM = "products_mvvm"
    const val PRODUCTS_MVI = "products_mvi"

    const val PRODUCT_DETAILS = "product_details"
    const val PRODUCT_ID = "productId"

    const val PRODUCT_DETAILS_ROUTE = "$PRODUCT_DETAILS/{$PRODUCT_ID}"

    fun productDetails(productId: Long): String {
        return "$PRODUCT_DETAILS/$productId"
    }
}