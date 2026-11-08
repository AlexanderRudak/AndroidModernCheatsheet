package dev.rudak.androidcheatsheet.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface RemoteConfigDestination {
    fun NavGraphBuilder.register(
        navController: NavHostController
    )
}
