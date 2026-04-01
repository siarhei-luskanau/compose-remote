package template.ui.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface RemoteDocumentRenderer {
    @Composable
    fun Render(
        bytes: ByteArray,
        hashCode: Int,
        modifier: Modifier,
    )
}
