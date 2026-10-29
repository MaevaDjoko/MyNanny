package com.example.mynanny.domain.signup

data class SignUpRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    /** Optional: empty when not provided. */
    val phone: String,
    val password: String,
)
