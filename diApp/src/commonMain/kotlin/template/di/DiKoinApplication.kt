package template.di

import org.koin.core.annotation.KoinApplication
import template.core.common.CoreCommonCommonModule
import template.core.pref.CorePrefCommonModule
import template.navigation.NavigationCommonModule
import template.ui.editor.EditorCommonModule
import template.ui.player.PlayerCommonModule
import template.ui.splash.SplashCommonModule

@KoinApplication(
    modules = [
        CoreCommonCommonModule::class,
        CorePrefCommonModule::class,
        DiCommonModule::class,
        EditorCommonModule::class,
        NavigationCommonModule::class,
        PlayerCommonModule::class,
        SplashCommonModule::class,
    ],
)
internal class DiKoinApplication
