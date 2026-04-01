package template.ui.player

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.koin.core.annotation.Single

@Single
internal class RemoteDocumentRendererWeb : RemoteDocumentRenderer {
    @Composable
    override fun Render(
        bytes: ByteArray,
        hashCode: Int,
        modifier: Modifier,
    ) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text("Remote Compose Player is not supported on this platform\nsize=${bytes.size}")
        }
    }
}
