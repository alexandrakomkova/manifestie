package com.example.manifestie.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.manifestie.core.QUOTE_WIDGET
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class DataStoreHelper: KoinComponent {
    private val dataStore: DataStore<Preferences> = getKoin().get()
    val quotePreferencesFlow: Flow<String> = flow {
        dataStore.data
            .map { preferences ->
                // Get our show completed value, defaulting to false if not set:
                val quotePreferencesFlow = preferences[QUOTE_WIDGET].toString()
                emit(quotePreferencesFlow)
            }
    }

    fun quote(): String {
        var q2: String? = "lol"
        CoroutineScope(Dispatchers.IO).launch {
            // dataStore.data.collect()
            q2 = dataStore.data.first()[QUOTE_WIDGET]

        }
        return q2 ?: "lll"
    }

    fun killmepls(): String {
        CoroutineScope(Dispatchers.IO).launch {
            val quotePreferences: String = flow {
                dataStore.data
                    .map { preferences ->
                        // Get our show completed value, defaulting to false if not set:
                        val quotePreferences = preferences[QUOTE_WIDGET].toString()
                        emit(quotePreferences)
                    }
            }.first()


        }
        return "quotePreferences"
    }
}

//    fun getQuoteFromDataStoreFlow(): String {
//        val dataStore: DataStore<Preferences> = getKoin().get()
//        var quote = "not working"
//        CoroutineScope(Dispatchers.IO).launch {
//            dataStore.data.collect {
//                quote = it[QUOTE_WIDGET].toString()
//            }
//        }
//
//        return quote
//    }

//    fun getQuote() {
//        val dataStore: DataStore<Preferences> = getKoin().get()
//
//        var quote = "default"
//        val userPreferencesFlow: Flow<String> = flow {
//            dataStore.data
//                .map { preferences ->
//                    // Get our show completed value, defaulting to false if not set:
//                    val userPreferencesFlow = preferences[QUOTE_WIDGET].toString() ?: "false"
//                    emit(userPreferencesFlow)
//                }
//        }
//
//    }
