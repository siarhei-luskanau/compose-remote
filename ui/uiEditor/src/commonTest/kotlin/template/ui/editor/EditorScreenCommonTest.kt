package template.ui.editor

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class EditorScreenCommonTest {
    @Test
    fun simpleCheck() =
        runComposeUiTest {
            setContent { EditorScreenPreview() }
            waitForIdle()
            onRoot().printToLog("StartTag")
            onNodeWithText("Loading…").assertIsDisplayed()
        }
}
