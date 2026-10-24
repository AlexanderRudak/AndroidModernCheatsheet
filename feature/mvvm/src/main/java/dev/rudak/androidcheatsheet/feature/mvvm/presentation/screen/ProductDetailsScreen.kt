package dev.rudak.androidcheatsheet.feature.mvvm.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.rudak.androidcheatsheet.core.ui.components.ErrorView
import dev.rudak.androidcheatsheet.core.ui.components.LoadingView
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.state.ProductDetailsNews
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.state.ProductDetailsUiState
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.viewmodel.ProductDetailsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ProductDetailsScreen(
    productId: Long,
    onBackClick: () -> Unit,
    viewModel: ProductDetailsViewModel = koinViewModel {
        parametersOf(productId)
    },
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

//    LaunchedEffect(productId) {
//        viewModel.loadProduct()
//    }

    LaunchedEffect(Unit) {
        viewModel.news.collect { news ->
            when (news) {
                ProductDetailsNews.ShowHistorySaveError -> {
                    snackbarHostState.showSnackbar(
                        message = "Не удалось сохранить товар в истории просмотров",
                    )
                }
            }
        }
    }

    Box {
        when (val state = uiState) {
            ProductDetailsUiState.Loading -> LoadingView()

            is ProductDetailsUiState.Error -> ErrorView(message = state.message)

            is ProductDetailsUiState.Content -> {
                Column(
                    modifier = Modifier.statusBarsPadding().padding(16.dp),
                ) {
                    Text(
                        text = state.product.title,
                        style = MaterialTheme.typography.titleLarge,
                    )

                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = state.product.description,
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = state.product.priceText,
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Button(
                        modifier = Modifier.padding(top = 24.dp),
                        onClick = onBackClick,
                    ) {
                        Text("Back")
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
