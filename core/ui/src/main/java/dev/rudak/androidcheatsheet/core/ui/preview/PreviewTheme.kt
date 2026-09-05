package dev.rudak.androidcheatsheet.core.ui.preview

import androidx.compose.runtime.Composable
import dev.rudak.androidcheatsheet.core.ui.theme.AndroidModernCheatsheetTheme

@Composable
fun PreviewTheme(
    content: @Composable () -> Unit,
) {
    AndroidModernCheatsheetTheme(
        dynamicColor = false,
        content = content,
    )
}