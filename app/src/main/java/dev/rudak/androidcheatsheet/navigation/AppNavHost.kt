package dev.rudak.androidcheatsheet.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.rudak.androidcheatsheet.feature.mvi.presentation.screen.MviScreen
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.screen.MvvmScreen
import dev.rudak.androidcheatsheet.feature.mvvm.presentation.screen.ProductDetailsScreen

@Composable
fun AppNavHost() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.PRODUCTS_MVI,
    ) {

        composable(Destinations.PRODUCTS_MVVM) {
            MvvmScreen(
                onNavigate = { productId ->
                    navController.navigate(Destinations.productDetails(productId))
                },
            )
        }

        composable(Destinations.PRODUCTS_MVI) {
            MviScreen(
                onNavigateToProductDetails = { productId ->
                    navController.navigate(
                        Destinations.productDetails(productId)
                    )
                },
            )
        }

        composable(
            route = Destinations.PRODUCT_DETAILS_ROUTE,
            arguments = listOf(
                navArgument(Destinations.PRODUCT_ID) {
                    type = NavType.LongType
                },
            ),
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getLong(Destinations.PRODUCT_ID) ?: return@composable

            ProductDetailsScreen(
                productId = productId,
                onBackClick = navController::popBackStack,
            )
        }
    }
}