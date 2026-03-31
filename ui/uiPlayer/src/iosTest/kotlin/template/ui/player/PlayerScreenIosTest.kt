package template.ui.player

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class, ExperimentalRoborazziApi::class)
internal class PlayerScreenIosTest {
    @Test
    fun loading() =
        runComposeUiTest {
            setContent { PlayerScreenLoadingPreview() }
            waitForIdle()
            onRoot().captureRoboImage(this, filePath = "template.ui.player.PlayerScreenIosTest.loading.png")
        }

    @Test
    fun noDocument() =
        runComposeUiTest {
            setContent { PlayerScreenNoDocumentPreview() }
            waitForIdle()
            onRoot().captureRoboImage(this, filePath = "template.ui.player.PlayerScreenIosTest.noDocument.png")
        }

    @Test
    fun hasDocument() =
        runComposeUiTest {
            setContent { PlayerScreenHasDocumentPreview() }
            waitForIdle()
            onRoot().captureRoboImage(this, filePath = "template.ui.player.PlayerScreenIosTest.hasDocument.png")
        }
}
