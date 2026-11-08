package dev.rudak.androidcheatsheet

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.rudak.androidcheatsheet.core.remoteconfig.DebugConfigRepository
import dev.rudak.androidcheatsheet.core.remoteconfig.RemoteConfigFeatures
import dev.rudak.androidcheatsheet.core.remoteconfig.RemoteConfigRepository
import dev.rudak.androidcheatsheet.core.remoteconfig.UseMviFeature
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebugRemoteConfigScreen(
    features: RemoteConfigFeatures,
    repository: RemoteConfigRepository,
    debugRepository: DebugConfigRepository,
    onBackClick: (Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()

    var useMvi by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val override = debugRepository.getOverride(UseMviFeature.key)

        useMvi = override?.toBooleanStrictOrNull()
            ?: repository.getString(UseMviFeature.key)
                .takeIf { it.isNotEmpty() }
                ?.toBooleanStrictOrNull()
                    ?: UseMviFeature.debugValue.toBoolean()

        isLoading = false
    }

    // Системная кнопка Android и стрелка в Toolbar
    // выполняют одинаковое действие.
    BackHandler(enabled = !isLoading && !isSaving) {
        onBackClick(useMvi)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Remote Config") },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackClick(useMvi) },
                        enabled = !isLoading && !isSaving
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = "Back",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isLoading || isSaving) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Use MVI")

                Switch(
                    checked = useMvi,
                    enabled = !isLoading && !isSaving,
                    onCheckedChange = { checked ->
                        scope.launch {
                            isSaving = true
                            try {
                                debugRepository.setOverride(
                                    UseMviFeature.key,
                                    checked.toString()
                                )
                                useMvi = checked
                            } finally {
                                isSaving = false
                            }
                        }
                    }
                )
            }

            TextButton(
                enabled = !isLoading && !isSaving,
                onClick = {
                    scope.launch {
                        isSaving = true
                        try {
                            debugRepository.clearOverride(UseMviFeature.key)

                            useMvi = repository
                                .getString(UseMviFeature.key)
                                .takeIf { it.isNotEmpty() }
                                ?.toBooleanStrictOrNull()
                                ?: UseMviFeature.debugValue.toBoolean()
                        } finally {
                            isSaving = false
                        }
                    }
                }
            ) {
                Text("Reset to Firebase")
            }
        }
    }
}
