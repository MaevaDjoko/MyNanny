package com.example.mynanny.domain.session

import com.example.mynanny.data.session.repository.SessionRepository
import javax.inject.Inject

class RefreshCurrentUserUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
) {
    /** @throws com.example.mynanny.domain.common.AuthException */
    suspend operator fun invoke() = sessionRepository.refreshCurrentUser()
}
