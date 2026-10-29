package com.example.mynanny.domain.signup

import javax.inject.Inject

/** Business rules of the sign-up form. Returns every invalid field (empty set = valid). */
class ValidateSignUpUseCase @Inject constructor() {

    operator fun invoke(request: SignUpRequest): Set<SignUpFieldError> = buildSet {
        if (request.firstName.isBlank()) add(SignUpFieldError.FIRST_NAME_REQUIRED)
        if (request.lastName.isBlank()) add(SignUpFieldError.LAST_NAME_REQUIRED)
        if (!EMAIL_REGEX.matches(request.email.trim())) add(SignUpFieldError.EMAIL_INVALID)
        if (request.phone.isNotBlank() && !PHONE_REGEX.matches(request.phone.trim())) add(SignUpFieldError.PHONE_INVALID)
        if (request.password.length < MIN_PASSWORD_LENGTH) add(SignUpFieldError.PASSWORD_TOO_SHORT)
    }

    companion object {
        const val MIN_PASSWORD_LENGTH = 8
        private val EMAIL_REGEX = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]{2,}$")

        // At least 10 digits, optional leading +, spaces/dots/dashes allowed: "06 12 34 56 78", "+33612345678"
        private val PHONE_REGEX = Regex("^\\+?(?:[ .-]?\\d){10,15}$")
    }
}
