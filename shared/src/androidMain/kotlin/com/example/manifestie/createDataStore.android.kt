package com.example.manifestie

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.manifestie.data.datastore.DATASTORE_FILENAME
import com.example.manifestie.data.datastore.createDataStore

fun createDataStore(context: Context): DataStore<Preferences> {
    return createDataStore {
        context.filesDir.resolve(DATASTORE_FILENAME).absolutePath
    }
}