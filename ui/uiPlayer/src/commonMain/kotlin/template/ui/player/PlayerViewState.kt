package template.ui.player

sealed interface PlayerViewState {
    data object Loading : PlayerViewState

    data object NoDocument : PlayerViewState

    data class HasDocument(
        val bytes: ByteArray,
    ) : PlayerViewState
}
