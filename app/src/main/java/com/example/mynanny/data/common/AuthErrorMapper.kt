package com.example.mynanny.data.common

import com.example.mynanny.domain.common.AuthException
import io.github.jan.supabase.auth.exception.AuthErrorCode
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.exception.AuthWeakPasswordException
import io.github.jan.supabase.exceptions.HttpRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.CancellationException

/** Runs a Supabase call and translates its exceptions into domain [AuthException]s. */
inline fun <T> mapAuthErrors(block: () -> T): T = try {
    block()
} catch (e: CancellationException) {
    throw e
} catch (e: AuthWeakPasswordException) {
    throw AuthException.WeakPassword()
} catch (e: AuthRestException) {
    throw when (e.errorCode) {
        AuthErrorCode.UserAlreadyExists, AuthErrorCode.EmailExists -> AuthException.EmailAlreadyUsed()
        AuthErrorCode.WeakPassword -> AuthException.WeakPassword()
        AuthErrorCode.EmailAddressInvalid, AuthErrorCode.ValidationFailed -> AuthException.InvalidEmail()
        AuthErrorCode.OverRequestRateLimit, AuthErrorCode.OverEmailSendRateLimit -> AuthException.TooManyAttempts()
        else -> AuthException.Unknown(e)
    }
} catch (e: HttpRequestException) {
    throw AuthException.Network(e)
} catch (e: HttpRequestTimeoutException) {
    throw AuthException.Network(e)
} catch (e: AuthException) {
    throw e
} catch (e: Exception) {
    throw AuthException.Unknown(e)
}
