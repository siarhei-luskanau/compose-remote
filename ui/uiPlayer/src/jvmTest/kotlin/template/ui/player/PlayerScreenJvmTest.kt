package template.ui.player

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class PlayerScreenJvmTest {
    @Test
    fun loading() =
        runDesktopComposeUiTest {
            setContent { PlayerScreenLoadingPreview() }
            waitForIdle()
            onRoot().captureRoboImage()
        }

    @Test
    fun noDocument() =
        runDesktopComposeUiTest {
            setContent { PlayerScreenNoDocumentPreview() }
            waitForIdle()
            onRoot().captureRoboImage()
        }

    @Test
    fun hasDocument() =
        runDesktopComposeUiTest {
            setContent { PlayerScreenHasDocumentPreview() }
            waitForIdle()
            onRoot().captureRoboImage()
        }
}
