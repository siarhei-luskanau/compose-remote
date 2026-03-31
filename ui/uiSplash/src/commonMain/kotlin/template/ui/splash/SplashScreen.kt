package template.ui.splash

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SplashScreen(viewModel: SplashViewModel) {
    SplashContent(
        viewStateFlow = viewModel.viewState,
        onEvent = viewModel::onEvent,
    )
}

@Composable
internal fun SplashContent(
    viewStateFlow: StateFlow<SplashViewState>,
    onEvent: (SplashViewEvent) -> Unit,
) {
    val viewState = viewStateFlow.collectAsState()
    Scaffold {
        Text("Splash: $viewState")
    }
    LaunchedEffect(Unit) {
        onEvent(SplashViewEvent.Launched)
    }
}
