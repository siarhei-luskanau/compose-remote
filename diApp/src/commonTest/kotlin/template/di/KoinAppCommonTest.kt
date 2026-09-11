package template.di

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class KoinAppCommonTest {
    @Test
    fun simpleCheck() {
        runComposeUiTest {
            mainClock.autoAdvance = false
            setContent { KoinApp() }
            onNodeWithText("Splash:", substring = true).assertIsDisplayed()

            mainClock.autoAdvance = true
            waitForIdle()
            awaitIdle()

            onNodeWithText("No document saved yet").assertIsDisplayed()
        }
    }
}
