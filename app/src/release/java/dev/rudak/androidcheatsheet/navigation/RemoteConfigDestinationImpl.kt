package dev.rudak.androidcheatsheet.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

class RemoteConfigDestinationImpl : RemoteConfigDestination {

    override fun NavGraphBuilder.register(
        navController: NavHostController
    ) = Unit
}
