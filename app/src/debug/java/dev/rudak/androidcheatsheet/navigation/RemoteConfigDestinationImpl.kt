package dev.rudak.androidcheatsheet.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import dev.rudak.androidcheatsheet.DebugRemoteConfigScreen
import dev.rudak.androidcheatsheet.core.remoteconfig.DebugConfigRepository
import dev.rudak.androidcheatsheet.core.remoteconfig.RemoteConfigFeatures
import dev.rudak.androidcheatsheet.core.remoteconfig.RemoteConfigRepository

class RemoteConfigDestinationImpl(
    private val features: RemoteConfigFeatures,
    private val repository: RemoteConfigRepository,
    private val debugRepository: DebugConfigRepository
) : RemoteConfigDestination {

    override fun NavGraphBuilder.register(
        navController: NavHostController
    ) {
        composable(Destinations.DEBUG_REMOTE_CONFIG) {
            DebugRemoteConfigScreen(
                features = features,
                repository = repository,
                debugRepository = debugRepository,
                onBackClick = { useMvi ->
                    val destination = if (useMvi) {
                        Destinations.PRODUCTS_MVI
                    } else {
                        Destinations.PRODUCTS_MVVM
                    }

                    navController.navigate(destination) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )

        }
    }
}

