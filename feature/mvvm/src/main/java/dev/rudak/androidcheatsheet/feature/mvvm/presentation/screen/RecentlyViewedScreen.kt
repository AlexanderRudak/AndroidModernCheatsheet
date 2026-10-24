package dev.rudak.androidcheatsheet.feature.mvvm.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.rudak.androidcheatsheet.core.ui.R
import dev.rudak.androidcheatsheet.core.ui.components.ErrorView
import dev.rudak.androidcheatsheet.core.ui.components.LoadingView
import dev.rudak.androidcheatsheet.core.ui.model.ProductCardUiModel
import dev.rudak.androidcheatsheet.core.ui.theme.ProductCardBackground
import dev.rudak.androidcheatsheet.core.ui.theme.ProductCardBorder
import dev.rudak.androidcheatsheet.core.ui.theme.ProductPriceColor
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.state.RecentlyViewedNews
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.state.RecentlyViewedUiState
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.viewmodel.RecentlyViewedViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecentlyViewedScreen(
    onProductClick: (Long) -> Unit,
    onBackClick: () -> Unit,
    viewModel: RecentlyViewedViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.news.collect { news ->
            when (news) {
                RecentlyViewedNews.ShowClearHistoryError -> {
                    snackbarHostState.showSnackbar(
                        message = "Не удалось очистить историю",
                    )
                }
            }
        }
    }

    RecentlyViewedContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onProductClick = onProductClick,
        onBackClick = onBackClick,
        onClearConfirmed = viewModel::onClearHistoryClick,
    )
}

@Composable
fun RecentlyViewedContent(
    uiState: RecentlyViewedUiState,
    snackbarHostState: SnackbarHostState,
    onProductClick: (Long) -> Unit,
    onBackClick: () -> Unit,
    onClearConfirmed: () -> Unit,
) {
    var showClearDialog by rememberSaveable { mutableStateOf(false) }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text(text = "Очистить историю?") },
            text = { Text(text = "Все просмотренные товары будут удалены из истории. Избранное не изменится.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showClearDialog = false
                        onClearConfirmed()
                    },
                ) {
                    Text(text = "Очистить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text(text = "Отмена")
                }
            },
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .padding(16.dp),
        ) {
            RecentlyViewedHeader(
                showClearButton = uiState is RecentlyViewedUiState.Content,
                onBackClick = onBackClick,
                onClearClick = { showClearDialog = true },
            )

            when (uiState) {
                RecentlyViewedUiState.Loading -> LoadingView()

                is RecentlyViewedUiState.Error -> ErrorView(message = uiState.message)

                RecentlyViewedUiState.Empty -> {
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = "No recently viewed products",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }

                is RecentlyViewedUiState.Content -> {
                    LazyColumn {
                        items(
                            items = uiState.products,
                            key = { product -> product.id },
                        ) { product ->
                            RecentlyViewedItem(
                                product = product,
                                onProductClick = onProductClick,
                            )
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
        )
    }
}

@Composable
private fun RecentlyViewedHeader(
    showClearButton: Boolean,
    onBackClick: () -> Unit,
    onClearClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
            )
        }

        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            text = "Recently viewed",
            style = MaterialTheme.typography.titleLarge,
        )

        if (showClearButton) {
            IconButton(
                modifier = Modifier.size(48.dp),
                onClick = onClearClick,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_lucide_trash),
                    contentDescription = "Clear history",
                )
            }
        }
    }
}

@Composable
private fun RecentlyViewedItem(
    product: ProductCardUiModel,
    onProductClick: (Long) -> Unit,
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
    }
}
