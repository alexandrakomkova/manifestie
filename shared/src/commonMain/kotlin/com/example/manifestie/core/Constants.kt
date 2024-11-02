package com.example.manifestie.core

import androidx.datastore.preferences.core.stringPreferencesKey

const val NETWORK_TIME_OUT = 6_000L
const val ZEN_QUOTES_RANDOM_URL = "https://zenquotes.io/api/random"

const val UNSPLASH_RANDOM_URL = "https://api.unsplash.com/photos/random"

val QUOTE_WIDGET = stringPreferencesKey("quote_widget")

const val FIRESTORE_CATEGORY_LIST = "CATEGORIES"
const val FIRESTORE_QUOTE_LIST = "quotes"

const val NAV_CATEGORY_DETAIL = "categoryId"