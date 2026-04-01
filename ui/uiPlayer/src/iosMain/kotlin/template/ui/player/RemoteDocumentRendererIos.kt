package template.ui.player

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.koin.core.annotation.Single

@Single
internal class RemoteDocumentRendererIos : RemoteDocumentRenderer {
    @Composable
    override fun Render(
        bytes: ByteArray,
        modifier: Modifier,
    ) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text("Remote Compose Player is not supported on this platform")
        }
    }
}
