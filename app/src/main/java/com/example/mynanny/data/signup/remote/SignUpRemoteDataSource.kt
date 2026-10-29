package com.example.mynanny.data.signup.remote

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.JsonObject
import javax.inject.Inject

class SignUpRemoteDataSource @Inject constructor(
    private val auth: Auth,
) {
    /**
     * Creates the account; [metadata] is stored in the user's `raw_user_meta_data`.
     * @return true when a session was opened (email confirmation disabled), false otherwise
     */
    suspend fun signUp(email: String, password: String, metadata: JsonObject): Boolean {
        auth.signUpWith(Email) {
            this.email = email
            this.password = password
            data = metadata
        }
        return auth.currentSessionOrNull() != null
    }
}
