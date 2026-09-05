package dev.rudak.androidcheatsheet.domain.usecase.shop

import dev.rudak.androidcheatsheet.domain.model.shop.ProductDetailsData
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetProductDetailsInteractor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getRecommendedProductsUseCase: GetRecommendedProductsUseCase,
) {

    suspend operator fun invoke(productId: Long): ProductDetailsData = coroutineScope {
        val productDeferred = async {
            getProductByIdUseCase(productId)
        }

        val recommendedProductsDeferred = async {
            getRecommendedProductsUseCase(productId)
        }

        ProductDetailsData(
            product = productDeferred.await(),
            recommendedProducts = recommendedProductsDeferred.await(),
        )
    }
}