package com.example.mynanny.domain.session

import com.example.mynanny.data.session.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class ObserveCurrentUserUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
) {
    /** The logged-in user's profile, from the device first; updated each time it is refreshed. */
    operator fun invoke(): Flow<User?> = sessionRepository.observeCurrentUser().distinctUntilChanged()
}
