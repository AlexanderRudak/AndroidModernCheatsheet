package dev.rudak.androidcheatsheet.domain.repository.paging

import androidx.paging.PagingData
import dev.rudak.androidcheatsheet.domain.model.shop.Product
import kotlinx.coroutines.flow.Flow

//отвечает только за Paging 3;
interface ProductPagingRepository {

    fun getPagedProducts(): Flow<PagingData<Product>>
}