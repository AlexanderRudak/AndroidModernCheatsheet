package dev.rudak.androidcheatsheet.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.rudak.androidcheatsheet.domain.model.shop.Product
//import dev.rudak.androidcheatsheet.domain.repository.ProductRepository
import dev.rudak.androidcheatsheet.domain.repository.shop.ProductRepository

class ProductsPagingSource(
    private val productRepository: ProductRepository,
) : PagingSource<Int, Product>() {

    override suspend fun load(
        params: LoadParams<Int>,
    ): LoadResult<Int, Product> {
        return try {
            val page = params.key ?: FIRST_PAGE

            val result = productRepository.getProducts(
                page = page,
                pageSize = params.loadSize,
            )

            LoadResult.Page(
                data = result.products,
                prevKey = if (page == FIRST_PAGE) {
                    null
                } else {
                    page - 1
                },
                nextKey = if (result.currentPage < result.totalPages) {
                    result.currentPage + 1
                } else {
                    null
                },
            )
        } catch (throwable: Throwable) {
            LoadResult.Error(throwable)
        }
    }

    override fun getRefreshKey(
        state: PagingState<Int, Product>,
    ): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition)

        return anchorPage?.prevKey?.plus(1)
            ?: anchorPage?.nextKey?.minus(1)
    }

    private companion object {
        const val FIRST_PAGE = 1
    }
}