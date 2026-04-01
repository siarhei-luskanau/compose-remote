package template.ui.player

import android.annotation.SuppressLint
import androidx.compose.remote.player.compose.ExperimentalRemotePlayerApi
import androidx.compose.remote.player.compose.RemoteDocumentPlayer
import androidx.compose.remote.player.core.RemoteDocument
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.koin.core.annotation.Single

@Single
internal class RemoteDocumentRendererAndroid : RemoteDocumentRenderer {
    @SuppressLint("RestrictedApi")
    @OptIn(ExperimentalRemotePlayerApi::class)
    @Composable
    override fun Render(
        bytes: ByteArray,
        hashCode: Int,
        modifier: Modifier,
    ) {
        val document = remember(hashCode) { RemoteDocument(bytes) }
        RemoteDocumentPlayer(
            document = document.document,
            documentWidth = document.width,
            documentHeight = document.height,
            modifier = modifier,
            onAction = { _, _ -> },
        )
    }
}
