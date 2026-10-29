package com.example.mynanny.testing

import com.example.mynanny.data.signup.repository.SignUpRepository
import com.example.mynanny.domain.signup.SignUpRequest
import com.example.mynanny.domain.signup.SignUpResult

class FakeSignUpRepository : SignUpRepository {
    var signUpResult: SignUpResult = SignUpResult.SIGNED_IN
    var signUpError: Exception? = null
    var lastSignUpRequest: SignUpRequest? = null
        private set

    override suspend fun signUp(request: SignUpRequest): SignUpResult {
        lastSignUpRequest = request
        signUpError?.let { throw it }
        return signUpResult
    }
}
