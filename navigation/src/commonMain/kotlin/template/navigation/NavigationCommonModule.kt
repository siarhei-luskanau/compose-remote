package template.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.annotation.Module
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import template.ui.common.resources.Res
import template.ui.common.resources.editor
import template.ui.common.resources.ic_editor
import template.ui.common.resources.ic_player
import template.ui.common.resources.player
import template.ui.editor.EditorScreen
import template.ui.player.PlayerScreen
import template.ui.splash.SplashScreen

@Module
@ComponentScan(value = ["template.navigation"])
class NavigationCommonModule

@OptIn(KoinExperimentalAPI::class)
val navigationModule =
    module {
        navigation<AppRoutes.Splash> {
            SplashScreen(viewModel = koinViewModel())
        }
        navigation<AppRoutes.Home> {
            AdaptiveHome()
        }
    }

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3AdaptiveApi::class,
    KoinExperimentalAPI::class,
)
@Composable
private fun AdaptiveHome() {
    val navigator = rememberListDetailPaneScaffoldNavigator<Unit>()
    val scope = rememberCoroutineScope()
    BackHandler(navigator.canNavigateBack()) {
        scope.launch { navigator.navigateBack() }
    }
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            item(
                icon = {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_player),
                        contentDescription = stringResource(Res.string.player),
                    )
                },
                label = { Text(stringResource(Res.string.player)) },
                selected = navigator.currentDestination?.pane != ListDetailPaneScaffoldRole.Detail,
                onClick = { scope.launch { navigator.navigateTo(ListDetailPaneScaffoldRole.List) } },
            )
            item(
                icon = {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_editor),
                        contentDescription = stringResource(Res.string.editor),
                    )
                },
                label = { Text(stringResource(Res.string.editor)) },
                selected = navigator.currentDestination?.pane == ListDetailPaneScaffoldRole.Detail,
                onClick = { scope.launch { navigator.navigateTo(ListDetailPaneScaffoldRole.Detail) } },
            )
        },
    ) {
        ListDetailPaneScaffold(
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            listPane = {
                AnimatedPane {
                    EditorScreen(viewModel = koinViewModel())
                }
            },
            detailPane = {
                AnimatedPane {
                    PlayerScreen(viewModel = koinViewModel())
                }
            },
        )
    }
}
