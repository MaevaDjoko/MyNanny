package com.example.mynanny.data.session.repository

import com.example.mynanny.domain.session.SessionStatus
import com.example.mynanny.domain.session.User
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    /** Emits the current session once it has been restored from storage, then every change. */
    fun observeSessionStatus(): Flow<SessionStatus>

    /** The logged-in user's profile from the local cache; null until it has been loaded once. */
    fun observeCurrentUser(): Flow<User?>

    /**
     * Reloads the profile from the server and updates the cache.
     * @throws com.example.mynanny.domain.common.AuthException
     */
    suspend fun refreshCurrentUser()

    /** Logs out and removes the cached profile. */
    suspend fun signOut()
}
