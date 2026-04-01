package template.ui.player

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.compose.koinInject
import template.ui.common.theme.AppTheme

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel,
    renderer: RemoteDocumentRenderer = koinInject(),
) {
    PlayerContent(
        viewStateFlow = viewModel.viewState,
        onEvent = viewModel::onEvent,
        renderDocument = renderer::Render,
    )
}

@Composable
internal fun PlayerContent(
    viewStateFlow: StateFlow<PlayerViewState>,
    onEvent: (PlayerViewEvent) -> Unit,
    renderDocument: @Composable (bytes: ByteArray, hashCode: Int, modifier: Modifier) -> Unit,
) {
    val viewState = viewStateFlow.collectAsState(PlayerViewState.Loading)
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (val state = viewState.value) {
            PlayerViewState.Loading -> Text("Loading…")
            PlayerViewState.NoDocument -> Text("No document saved yet")
            is PlayerViewState.HasDocument -> renderDocument(state.bytes, state.bytes.contentHashCode(), Modifier.fillMaxSize())
        }
    }
}

@Preview
@Composable
internal fun PlayerScreenLoadingPreview() =
    AppTheme {
        PlayerContent(
            viewStateFlow = MutableStateFlow(PlayerViewState.Loading),
            onEvent = {},
            renderDocument = { _, _, _ -> },
        )
    }

@Preview
@Composable
internal fun PlayerScreenNoDocumentPreview() =
    AppTheme {
        PlayerContent(
            viewStateFlow = MutableStateFlow(PlayerViewState.NoDocument),
            onEvent = {},
            renderDocument = { _, _, _ -> },
        )
    }

@Preview
@Composable
internal fun PlayerScreenHasDocumentPreview() =
    AppTheme {
        PlayerContent(
            viewStateFlow = MutableStateFlow(PlayerViewState.HasDocument(byteArrayOf(1, 2, 3))),
            onEvent = {},
            renderDocument = { _, _, _ -> Text("Document loaded") },
        )
    }
