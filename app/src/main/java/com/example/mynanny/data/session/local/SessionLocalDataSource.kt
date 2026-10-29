package com.example.mynanny.data.session.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** Keeps the logged-in user's profile on the device, so it can be shown instantly and offline. */
class SessionLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    val cachedUser: Flow<CachedUser?> = dataStore.data.map { prefs ->
        prefs[Keys.ID]?.let { id ->
            CachedUser(
                id = id,
                email = prefs[Keys.EMAIL].orEmpty(),
                firstName = prefs[Keys.FIRST_NAME].orEmpty(),
                lastName = prefs[Keys.LAST_NAME].orEmpty(),
                phone = prefs[Keys.PHONE].orEmpty(),
            )
        }
    }

    suspend fun save(user: CachedUser) {
        dataStore.edit { prefs ->
            prefs[Keys.ID] = user.id
            prefs[Keys.EMAIL] = user.email
            prefs[Keys.FIRST_NAME] = user.firstName
            prefs[Keys.LAST_NAME] = user.lastName
            prefs[Keys.PHONE] = user.phone
        }
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    private object Keys {
        val ID = stringPreferencesKey("user_id")
        val EMAIL = stringPreferencesKey("user_email")
        val FIRST_NAME = stringPreferencesKey("user_first_name")
        val LAST_NAME = stringPreferencesKey("user_last_name")
        val PHONE = stringPreferencesKey("user_phone")
    }
}
