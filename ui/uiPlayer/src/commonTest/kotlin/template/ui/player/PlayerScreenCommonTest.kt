package template.ui.player

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class PlayerScreenCommonTest {
    @Test
    fun loading() =
        runComposeUiTest {
            setContent { PlayerScreenLoadingPreview() }
            waitForIdle()
            onRoot().printToLog("StartTag")
            onNodeWithText("Loading…").assertIsDisplayed()
        }

    @Test
    fun noDocument() =
        runComposeUiTest {
            setContent { PlayerScreenNoDocumentPreview() }
            waitForIdle()
            onRoot().printToLog("StartTag")
            onNodeWithText("No document saved yet").assertIsDisplayed()
        }

    @Test
    fun hasDocument() =
        runComposeUiTest {
            setContent { PlayerScreenHasDocumentPreview() }
            waitForIdle()
            onRoot().printToLog("StartTag")
            onNodeWithText("Document loaded").assertIsDisplayed()
        }
}
