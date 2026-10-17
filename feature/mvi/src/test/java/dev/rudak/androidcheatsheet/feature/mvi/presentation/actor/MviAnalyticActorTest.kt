package dev.rudak.androidcheatsheet.feature.mvi.presentation.actor

import dev.rudak.androidcheatsheet.core.analytics.AnalyticEvent
import dev.rudak.androidcheatsheet.core.analytics.AnalyticEventKey
import dev.rudak.androidcheatsheet.core.analytics.AnalyticEventName
import dev.rudak.androidcheatsheet.core.analytics.AnalyticSenderController
import dev.rudak.androidcheatsheet.core.analytics.firebase.AnalyticEventValue
import dev.rudak.androidcheatsheet.feature.mvi.presentation.command.MviCommand
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class MviAnalyticActorTest {

    private val analyticController = mockk<AnalyticSenderController>(relaxed = true)
    private val actor = MviAnalyticActor(analyticController)

    @Test
    fun `GIVEN ProductClicked WHEN processed THEN send product analytics and emit no events`() = runTest {
        val events = actor.process(
            flowOf(MviCommand.Analytic.ProductClicked(productId = 5L))
        ).toList()

        assertTrue(events.isEmpty())
        verify(exactly = 1) {
            analyticController.sendEvent(
                AnalyticEvent(
                    name = AnalyticEventName.PRODUCT_CLICKED,
                    params = mapOf(AnalyticEventKey.PRODUCT_ID to 5L),
                )
            )
        }
    }

    @Test
    fun `GIVEN FavoriteClicked WHEN processed THEN send correct add and remove analytics`() = runTest {
        val events = actor.process(
            flowOf(
                MviCommand.Analytic.FavoriteClicked(productId = 5L, isFavorite = false),
                MviCommand.Analytic.FavoriteClicked(productId = 5L, isFavorite = true),
            )
        ).toList()

        assertTrue(events.isEmpty())
        verify(exactly = 1) {
            analyticController.sendEvent(
                AnalyticEvent(
                    name = AnalyticEventName.FAVORITE_CLICKED,
                    params = mapOf(
                        AnalyticEventKey.PRODUCT_ID to 5L,
                        AnalyticEventKey.ACTION to AnalyticEventValue.ADD,
                    ),
                )
            )
        }
        verify(exactly = 1) {
            analyticController.sendEvent(
                AnalyticEvent(
                    name = AnalyticEventName.FAVORITE_CLICKED,
                    params = mapOf(
                        AnalyticEventKey.PRODUCT_ID to 5L,
                        AnalyticEventKey.ACTION to AnalyticEventValue.REMOVE,
                    ),
                )
            )
        }
    }

    @Test
    fun `GIVEN action command WHEN processed THEN do not send analytics or emit events`() = runTest {
        val events = actor.process(
            flowOf(MviCommand.LoadProducts(page = 1, pageSize = 10))
        ).toList()

        assertTrue(events.isEmpty())
        verify(exactly = 0) { analyticController.sendEvent(any()) }
    }
}
