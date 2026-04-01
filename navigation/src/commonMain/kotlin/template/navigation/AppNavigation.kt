package template.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.navigation3.runtime.NavKey
import org.koin.core.annotation.Single
import template.ui.splash.SplashNavigationCallback

@Single
internal class AppNavigation : SplashNavigationCallback {
    val backStack = mutableStateListOf<NavKey>(AppRoutes.Splash)

    fun goBack() {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }

    override fun goHome() {
        backStack.add(AppRoutes.Home)
        backStack.remove(AppRoutes.Splash)
    }
}
