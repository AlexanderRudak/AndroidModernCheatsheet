package dev.rudak.androidcheatsheet.data.repository.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.rudak.androidcheatsheet.domain.model.shop.Product
import dev.rudak.androidcheatsheet.domain.repository.paging.ProductPagingRepository
import dev.rudak.androidcheatsheet.domain.repository.shop.ProductRepository
import kotlinx.coroutines.flow.Flow

class ProductPagingRepositoryImpl(
    private val productRepository: ProductRepository,
) : ProductPagingRepository {

    override fun getPagedProducts(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                ProductsPagingSource(
                    productRepository = productRepository,
                )
            },
        ).flow
    }

    private companion object {
        const val PAGE_SIZE = 10
    }
}