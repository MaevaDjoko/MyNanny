package com.example.mynanny.domain.common

import com.example.mynanny.domain.signup.SignUpFieldError

/** Authentication failures, independent of the backend (Supabase errors are translated in the data layer). */
sealed class AuthException(cause: Throwable? = null) : Exception(cause) {
    data class InvalidForm(val errors: Set<SignUpFieldError>) : AuthException()
    class EmailAlreadyUsed : AuthException()
    class WeakPassword : AuthException()
    class InvalidEmail : AuthException()
    class TooManyAttempts : AuthException()
    class Network(cause: Throwable) : AuthException(cause)
    class Unknown(cause: Throwable) : AuthException(cause)
}
