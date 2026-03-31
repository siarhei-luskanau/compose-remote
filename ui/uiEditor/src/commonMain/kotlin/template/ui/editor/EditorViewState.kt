package template.ui.editor

sealed interface EditorViewState {
    data object Loading : EditorViewState
}
