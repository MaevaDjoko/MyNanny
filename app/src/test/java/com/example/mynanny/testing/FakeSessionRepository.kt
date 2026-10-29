package com.example.mynanny.testing

import com.example.mynanny.data.session.repository.SessionRepository
import com.example.mynanny.domain.session.SessionStatus
import com.example.mynanny.domain.session.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeSessionRepository : SessionRepository {
    val sessionStatus = MutableSharedFlow<SessionStatus>(replay = 1)

    /** Simulates the local cache. */
    val cachedUser = MutableStateFlow<User?>(null)

    /** What the server returns on refresh. */
    var serverUser: User? = null
    var refreshError: Exception? = null
    var signOutError: Exception? = null

    override fun observeSessionStatus(): Flow<SessionStatus> = sessionStatus

    override fun observeCurrentUser(): Flow<User?> = cachedUser

    override suspend fun refreshCurrentUser() {
        refreshError?.let { throw it }
        cachedUser.value = serverUser
    }

    override suspend fun signOut() {
        cachedUser.value = null
        signOutError?.let { throw it }
    }
}
