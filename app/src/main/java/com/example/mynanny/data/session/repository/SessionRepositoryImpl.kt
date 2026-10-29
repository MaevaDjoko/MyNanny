package com.example.mynanny.data.session.repository

import com.example.mynanny.data.common.UserMetadata
import com.example.mynanny.data.common.mapAuthErrors
import com.example.mynanny.data.session.local.CachedUser
import com.example.mynanny.data.session.local.SessionLocalDataSource
import com.example.mynanny.data.session.remote.SessionRemoteDataSource
import com.example.mynanny.domain.session.SessionStatus
import com.example.mynanny.domain.session.User
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject
import io.github.jan.supabase.auth.status.SessionStatus as SupabaseSessionStatus

class SessionRepositoryImpl @Inject constructor(
    private val remoteDataSource: SessionRemoteDataSource,
    private val localDataSource: SessionLocalDataSource,
) : SessionRepository {

    // Maps Supabase's session states to the domain model; "Initializing" is not emitted.
    override fun observeSessionStatus(): Flow<SessionStatus> =
        remoteDataSource.sessionStatus().mapNotNull { status ->
            when (status) {
                is SupabaseSessionStatus.Initializing -> null
                is SupabaseSessionStatus.Authenticated ->
                    status.session.user?.id?.let(SessionStatus::Authenticated) ?: SessionStatus.Unauthenticated
                is SupabaseSessionStatus.NotAuthenticated,
                is SupabaseSessionStatus.RefreshFailure -> SessionStatus.Unauthenticated
            }
        }

    // Never shows another account's cached profile: the cache must match the current session.
    override fun observeCurrentUser(): Flow<User?> =
        localDataSource.cachedUser.map { cached ->
            cached?.takeIf { it.id == remoteDataSource.currentUserId() }?.toDomain()
        }

    override suspend fun refreshCurrentUser() {
        if (remoteDataSource.currentUserId() == null) {
            localDataSource.clear()
            return
        }
        val user = mapAuthErrors { remoteDataSource.fetchCurrentUser() }
        localDataSource.save(user.toCachedUser())
    }

    override suspend fun signOut() {
        try {
            mapAuthErrors { remoteDataSource.signOut() }
        } finally {
            // Personal data must not stay on the device, even if the server call failed.
            localDataSource.clear()
        }
    }

    private fun UserInfo.toCachedUser() = CachedUser(
        id = id,
        email = email.orEmpty(),
        firstName = metadata(UserMetadata.FIRST_NAME),
        lastName = metadata(UserMetadata.LAST_NAME),
        phone = metadata(UserMetadata.PHONE),
    )

    private fun UserInfo.metadata(key: String): String =
        userMetadata?.get(key)?.jsonPrimitive?.contentOrNull.orEmpty()

    private fun CachedUser.toDomain() = User(id, email, firstName, lastName, phone)
}
