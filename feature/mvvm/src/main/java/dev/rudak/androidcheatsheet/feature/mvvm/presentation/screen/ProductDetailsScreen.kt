package dev.rudak.androidcheatsheet.feature.mvvm.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.rudak.androidcheatsheet.core.ui.components.ErrorView
import dev.rudak.androidcheatsheet.core.ui.components.LoadingView
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

//    LaunchedEffect(productId) {
//        viewModel.loadProduct()
//    }

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
}