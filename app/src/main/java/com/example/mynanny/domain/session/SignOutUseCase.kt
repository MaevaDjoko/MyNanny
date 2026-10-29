package com.example.mynanny.domain.session

import com.example.mynanny.data.session.repository.SessionRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
) {
    suspend operator fun invoke() = sessionRepository.signOut()
}
