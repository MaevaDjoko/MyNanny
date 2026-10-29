package com.example.mynanny.domain.session

sealed interface SessionStatus {
    data class Authenticated(val userId: String) : SessionStatus
    data object Unauthenticated : SessionStatus
}
