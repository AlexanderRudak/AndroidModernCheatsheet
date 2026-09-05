package dev.rudak.androidcheatsheet.domain.usecase.shop

import androidx.paging.PagingData
import dev.rudak.androidcheatsheet.domain.model.shop.Product
import dev.rudak.androidcheatsheet.domain.repository.paging.ProductPagingRepository
import kotlinx.coroutines.flow.Flow

class GetPagedProductsUseCase(
    private val productPagingRepository: ProductPagingRepository,
) {

    operator fun invoke(): Flow<PagingData<Product>> {
        return productPagingRepository.getPagedProducts()
    }
}