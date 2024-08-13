package com.example.manifestie

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.manifestie.presentation.RandomQuoteScreen
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier


@Composable
fun App(
    //prefs: DataStore<Preferences>
) {

    ManifestieTheme {

        Napier.base(DebugAntilog())

        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            RandomQuoteScreen()
        }
    }

    /*KoinApplication(application = {
        modules(
            getBaseModules(),
        )
    }) {
        ManifestieTheme {

            Napier.base(DebugAntilog())

            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                RandomQuoteScreen()
            }
        }
    }*/

}
