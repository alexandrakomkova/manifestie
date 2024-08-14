package com.example.manifestie

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.manifestie.data.datastore.DATASTORE_FILENAME
import com.example.manifestie.data.datastore.createDataStore
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
fun createDataStore(): DataStore<Preferences> {
    return createDataStore {
        val directory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error= null
        )
        requireNotNull(directory).path + "/$DATASTORE_FILENAME"
    }
}