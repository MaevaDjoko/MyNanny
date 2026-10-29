package com.example.mynanny.data.session.local

/** Profile of the logged-in user as stored on the device. */
data class CachedUser(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
)
