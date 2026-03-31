package template.ui.editor

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
import template.ui.common.theme.AppTheme

@Composable
fun EditorScreen(viewModel: EditorViewModel) {
    EditorContent(
        viewStateFlow = viewModel.viewState,
        onEvent = viewModel::onEvent,
    )
}

@Composable
internal fun EditorContent(
    viewStateFlow: StateFlow<EditorViewState>,
    onEvent: (EditorViewEvent) -> Unit,
) {
    val viewState = viewStateFlow.collectAsState()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (viewState.value) {
            EditorViewState.Loading -> Text("Loading…")
        }
    }
}

@Preview
@Composable
internal fun EditorScreenPreview() =
    AppTheme {
        EditorContent(
            viewStateFlow = MutableStateFlow(EditorViewState.Loading),
            onEvent = {},
        )
    }
