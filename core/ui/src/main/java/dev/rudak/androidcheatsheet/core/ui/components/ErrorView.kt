package dev.rudak.androidcheatsheet.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.rudak.androidcheatsheet.core.ui.theme.ErrorTitleColor

//@Composable
//fun ErrorView(
//    message: String,
//    modifier: Modifier = Modifier,
//) {
//    Column(
//        modifier = modifier.statusBarsPadding().padding(16.dp),
//    ) {
//        Text(
//            text = "Error",
//            style = MaterialTheme.typography.titleMedium,
//            color = MaterialTheme.colorScheme.error,
//        )
//
//        Text(
//            modifier = Modifier.padding(top = 8.dp),
//            text = message,
//            style = MaterialTheme.typography.bodyMedium,
//        )
//    }
//}

@Composable
fun ErrorView(
    message: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Error",
            style = MaterialTheme.typography.headlineSmall,
            color = ErrorTitleColor,
        )

        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun ErrorContent(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        ErrorView(
            message = message,
        )

        Button(
            modifier = Modifier.padding(top = 24.dp),
            onClick = onRetryClick,
        ) {
            Text(text = "Retry")
        }
    }
}