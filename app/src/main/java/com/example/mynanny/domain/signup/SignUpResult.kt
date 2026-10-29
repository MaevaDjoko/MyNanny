package com.example.mynanny.domain.signup

enum class SignUpResult {
    /** The account is created and the user is logged in. */
    SIGNED_IN,

    /** The account is created but the user must click the link sent by email before logging in. */
    EMAIL_CONFIRMATION_REQUIRED,
}
