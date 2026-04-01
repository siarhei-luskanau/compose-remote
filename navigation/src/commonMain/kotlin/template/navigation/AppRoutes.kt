package template.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

internal sealed interface AppRoutes : NavKey {
    @Serializable
    data object Splash : AppRoutes

    @Serializable
    data object Home : AppRoutes

    @Serializable
    data object Player : AppRoutes

    @Serializable
    data object Editor : AppRoutes
}
