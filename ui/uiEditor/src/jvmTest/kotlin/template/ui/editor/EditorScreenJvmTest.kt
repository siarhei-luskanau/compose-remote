package template.ui.editor

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class EditorScreenJvmTest {
    @Test
    fun empty() =
        runDesktopComposeUiTest {
            setContent { EditorScreenEmptyPreview() }
            waitForIdle()
            onRoot().captureRoboImage()
        }

    @Test
    fun withElements() =
        runDesktopComposeUiTest {
            setContent { EditorScreenWithElementsPreview() }
            waitForIdle()
            onRoot().captureRoboImage()
        }
}
