package com.example.mynanny.domain.session

import com.example.mynanny.data.session.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class ObserveSessionStatusUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
) {
    operator fun invoke(): Flow<SessionStatus> = sessionRepository.observeSessionStatus().distinctUntilChanged()
}
