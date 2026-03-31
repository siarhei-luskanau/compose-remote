package template.ui.player

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.v2.runComposeUiTest
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import kotlin.test.Test

@GraphicsMode(GraphicsMode.Mode.NATIVE)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36], qualifiers = RobolectricDeviceQualifiers.SmallPhone)
@OptIn(ExperimentalTestApi::class)
internal class PlayerScreenAndroidTest {
    @Test
    fun loading() =
        runComposeUiTest {
            setContent { PlayerScreenLoadingPreview() }
            waitForIdle()
            onRoot().captureRoboImage()
        }

    @Test
    fun noDocument() =
        runComposeUiTest {
            setContent { PlayerScreenNoDocumentPreview() }
            waitForIdle()
            onRoot().captureRoboImage()
        }

    @Test
    fun hasDocument() =
        runComposeUiTest {
            setContent { PlayerScreenHasDocumentPreview() }
            waitForIdle()
            onRoot().captureRoboImage()
        }
}
