package dev.rudak.androidcheatsheet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import dev.rudak.androidcheatsheet.core.remoteconfig.RemoteConfigInteractor
import dev.rudak.androidcheatsheet.core.remoteconfig.UseMviFeature
import dev.rudak.androidcheatsheet.core.ui.theme.AndroidModernCheatsheetTheme
import dev.rudak.androidcheatsheet.navigation.AppNavHost
import dev.rudak.androidcheatsheet.navigation.Destinations
import dev.rudak.androidcheatsheet.navigation.RemoteConfigDestination
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val remoteConfigInteractor: RemoteConfigInteractor by inject()
    private val remoteConfigDestination: RemoteConfigDestination by inject()

    private var startDestination by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AndroidModernCheatsheetTheme {
                val destination = startDestination

                if (destination == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    AppNavHost(
                        startDestination = destination,
                        remoteConfigDestination = remoteConfigDestination
                    )
                }
            }
        }

        lifecycleScope.launch {
            remoteConfigInteractor.fetch()

            val useMvi = remoteConfigInteractor
                .getBoolean(UseMviFeature)

            startDestination = if (useMvi) {
                Destinations.PRODUCTS_MVI
            } else {
                Destinations.PRODUCTS_MVVM
            }
        }
    }
}
