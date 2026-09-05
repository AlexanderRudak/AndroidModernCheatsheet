package dev.rudak.androidcheatsheet.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import dev.rudak.androidcheatsheet.core.ui.model.ProductCardUiModel
import dev.rudak.androidcheatsheet.core.ui.theme.FavoriteIconColor
import dev.rudak.androidcheatsheet.core.ui.theme.ProductCardBackground
import dev.rudak.androidcheatsheet.core.ui.theme.ProductCardBorder
import dev.rudak.androidcheatsheet.core.ui.theme.ProductPriceColor

@Composable
fun PagedProductsDemoContent(
    title: String,
    description: String,
    products: LazyPagingItems<ProductCardUiModel>,
    showOnlyFavorites: Boolean,
    onShowOnlyFavoritesChange: (Boolean) -> Unit,
    onErrorClick: () -> Unit,
    onProductClick: (Long) -> Unit,
    onFavoriteClick: (Long) -> Unit,
) {
    val refreshState = products.loadState.refresh
    val appendState = products.loadState.append

    val error = when {
        refreshState is LoadState.Error -> refreshState.error
        appendState is LoadState.Error -> appendState.error
        else -> null
    }

    if (error != null) {
        ErrorContent(
            message = error.message ?: "Unknown error",
            onRetryClick = products::retry,
        )
        return
    }

    val isLoading =
        refreshState is LoadState.Loading ||
                appendState is LoadState.Loading

    val loadedProducts = products.itemSnapshotList.items

    val hasVisibleProducts = if (showOnlyFavorites) {
        loadedProducts.any { product -> product.isFavorite }
    } else {
        loadedProducts.isNotEmpty()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )

            Text(
                modifier = Modifier.padding(
                    top = 8.dp,
                    bottom = 16.dp,
                ),
                text = description,
                style = MaterialTheme.typography.bodyMedium,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Show only favorites",
                    style = MaterialTheme.typography.bodyMedium,
                )

                Switch(
                    checked = showOnlyFavorites,
                    onCheckedChange = onShowOnlyFavoritesChange,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = products::refresh,
                ) {
                    Text(text = "Reload")
                }

                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onErrorClick,
                ) {
                    Text(text = "Simulate error")
                }
            }

            if (
                !isLoading &&
                products.itemCount == 0
            ) {
                Text(
                    modifier = Modifier.padding(top = 24.dp),
                    text = "No products",
                    style = MaterialTheme.typography.bodyLarge,
                )
            } else if (
                !isLoading &&
                showOnlyFavorites &&
                !hasVisibleProducts
            ) {
                Text(
                    modifier = Modifier.padding(top = 24.dp),
                    text = "No favorite products",
                    style = MaterialTheme.typography.bodyLarge,
                )
            } else {
                LazyColumn {
                    items(
                        count = products.itemCount,
                        key = products.itemKey { product ->
                            product.id
                        },
                    ) { index ->
                        val product = products[index]

                        if (
                            product != null &&
                            (!showOnlyFavorites || product.isFavorite)
                        ) {
                            PagedProductDemoCard(
                                product = product,
                                onProductClick = onProductClick,
                                onFavoriteClick = onFavoriteClick,
                            )
                        }
                    }
                }
            }
        }

        if (isLoading) {
            LoadingView()
        }
    }
}

@Composable
private fun PagedProductDemoCard(
    product: ProductCardUiModel,
    onProductClick: (Long) -> Unit,
    onFavoriteClick: (Long) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable {
                onProductClick(product.id)
            },
        colors = CardDefaults.cardColors(
            containerColor = ProductCardBackground,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = ProductCardBorder,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleMedium,
                )

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                )

                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = product.priceText,
                    style = MaterialTheme.typography.titleMedium,
                    color = ProductPriceColor,
                )
            }

            IconButton(
                modifier = Modifier
                    .size(48.dp)
                    .testTag("favorite_button_${product.id}"),
                onClick = {
                    onFavoriteClick(product.id)
                },
            ) {
                Icon(
                    imageVector = if (product.isFavorite) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Outlined.FavoriteBorder
                    },
                    tint = if (product.isFavorite) {
                        FavoriteIconColor
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    contentDescription = null,
                )
            }
        }
    }
}