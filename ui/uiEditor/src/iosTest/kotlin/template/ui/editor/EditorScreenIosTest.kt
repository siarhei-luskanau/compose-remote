package template.ui.editor

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class, ExperimentalRoborazziApi::class)
internal class EditorScreenIosTest {
    @Test
    fun empty() =
        runComposeUiTest {
            setContent { EditorScreenEmptyPreview() }
            waitForIdle()
            onRoot().captureRoboImage(this, filePath = "template.ui.editor.EditorScreenIosTest.empty.png")
        }

    @Test
    fun withElements() =
        runComposeUiTest {
            setContent { EditorScreenWithElementsPreview() }
            waitForIdle()
            onRoot().captureRoboImage(this, filePath = "template.ui.editor.EditorScreenIosTest.withElements.png")
        }
}
