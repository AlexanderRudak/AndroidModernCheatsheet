package dev.rudak.androidcheatsheet.feature.mvi.presentation.actor

import dev.rudak.androidcheatsheet.feature.mvi.presentation.command.MviCommand
import dev.rudak.androidcheatsheet.feature.mvi.presentation.event.MviEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapNotNull
import dev.rudak.androidcheatsheet.core.analytics.AnalyticEvent
import dev.rudak.androidcheatsheet.core.analytics.AnalyticEventKey
import dev.rudak.androidcheatsheet.core.analytics.AnalyticEventName
import dev.rudak.androidcheatsheet.core.analytics.AnalyticSenderController
import dev.rudak.androidcheatsheet.core.analytics.firebase.AnalyticEventValue
import dev.rudak.androidcheatsheet.feature.mvi.core.MviActor

class MviAnalyticActor(
    private val analyticController: AnalyticSenderController,
) : MviActor<MviCommand, MviEvent> {

    override fun process(
        commands: Flow<MviCommand>,
    ): Flow<MviEvent> {
        return commands
            .filterIsInstance<MviCommand.Analytic>()
            .mapNotNull { command ->
                when (command) {
                    is MviCommand.Analytic.ProductClicked -> {
                        analyticController.sendEvent(
                            AnalyticEvent(
                                name = AnalyticEventName.PRODUCT_CLICKED,
                                params = mapOf(
                                    AnalyticEventKey.PRODUCT_ID to command.productId,
                                ),
                            ),
                        )
                    }

                    is MviCommand.Analytic.FavoriteClicked -> {
                        analyticController.sendEvent(
                            AnalyticEvent(
                                name = AnalyticEventName.FAVORITE_CLICKED,
                                params = mapOf(
                                    AnalyticEventKey.PRODUCT_ID to command.productId,
                                    AnalyticEventKey.ACTION to if (command.isFavorite) {
                                        AnalyticEventValue.REMOVE
                                    } else {
                                        AnalyticEventValue.ADD
                                    },
                                ),
                            ),
                        )
                    }

                }

                null
            }
    }
}

