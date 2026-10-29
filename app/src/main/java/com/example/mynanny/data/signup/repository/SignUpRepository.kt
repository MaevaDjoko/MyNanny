package com.example.mynanny.data.signup.repository

import com.example.mynanny.domain.signup.SignUpRequest
import com.example.mynanny.domain.signup.SignUpResult

interface SignUpRepository {
    /** @throws com.example.mynanny.domain.common.AuthException */
    suspend fun signUp(request: SignUpRequest): SignUpResult
}
