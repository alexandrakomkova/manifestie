package com.example.manifestie.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.manifestie.core.QUOTE_WIDGET
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

object DataStoreHelper: KoinComponent {
    private val dataStore: DataStore<Preferences> = getKoin().get()

    fun updateQuote(newQuote: String? = "kill me pls") {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences ->
                preferences[QUOTE_WIDGET] = newQuote.toString()
            }
            Napier.d(tag = "updateQuote", message = dataStore.data.first()[QUOTE_WIDGET].toString())
        }

        Napier.d(tag = "DataStoreHelper update", message = newQuote.toString())
    }

    val quotePreferencesFlow: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[QUOTE_WIDGET].toString()
        }

}
