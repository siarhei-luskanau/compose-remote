package template.ui.player

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class PlayerViewModel : ViewModel() {
    val viewState: StateFlow<PlayerViewState>
        field = MutableStateFlow<PlayerViewState>(PlayerViewState.Loading)

    fun onEvent(event: PlayerViewEvent) = Unit
}
