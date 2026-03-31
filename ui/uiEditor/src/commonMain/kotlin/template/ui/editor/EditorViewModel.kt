package template.ui.editor

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class EditorViewModel : ViewModel() {
    val viewState: StateFlow<EditorViewState>
        field = MutableStateFlow<EditorViewState>(EditorViewState.Loading)

    fun onEvent(event: EditorViewEvent) = Unit
}
