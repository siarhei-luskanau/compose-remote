package template.di

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class, ExperimentalRoborazziApi::class)
internal class KoinAppIosTest {
    @Test
    fun preview() =
        runComposeUiTest {
            setContent { KoinApp() }
            waitForIdle()
            onRoot().captureRoboImage(this, filePath = "template.di.KoinAppIosTest.preview.png")
        }
}
