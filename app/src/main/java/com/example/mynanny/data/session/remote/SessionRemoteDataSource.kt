package com.example.mynanny.data.session.remote

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** Session data from Supabase Auth (the session itself is persisted by the Supabase SDK). */
class SessionRemoteDataSource @Inject constructor(
    private val auth: Auth,
) {
    fun sessionStatus(): Flow<SessionStatus> = auth.sessionStatus

    /** Id of the logged-in user, known locally without any network call. */
    fun currentUserId(): String? = auth.currentUserOrNull()?.id

    /** Fetches the up-to-date user (profile metadata included) from the server. */
    suspend fun fetchCurrentUser(): UserInfo = auth.retrieveUserForCurrentSession(updateSession = true)

    suspend fun signOut() = auth.signOut()
}
