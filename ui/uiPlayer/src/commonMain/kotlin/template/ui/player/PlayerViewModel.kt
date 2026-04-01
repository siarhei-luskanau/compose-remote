package template.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.KoinViewModel
import org.koin.core.annotation.Provided
import template.core.pref.PrefService

@KoinViewModel
class PlayerViewModel(
    @Provided private val prefService: PrefService,
) : ViewModel() {
    val viewState: StateFlow<PlayerViewState> =
        prefService
            .getDocumentBytes()
            .map { bytes ->
                if (bytes != null) PlayerViewState.HasDocument(bytes) else PlayerViewState.NoDocument
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PlayerViewState.Loading,
            )

    fun onEvent(event: PlayerViewEvent) = Unit
}
