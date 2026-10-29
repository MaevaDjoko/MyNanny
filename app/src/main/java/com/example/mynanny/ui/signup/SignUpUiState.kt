package com.example.mynanny.ui.signup

import androidx.annotation.StringRes
import com.example.mynanny.domain.signup.SignUpFieldError
import com.example.mynanny.domain.signup.SignUpRequest
import com.example.mynanny.domain.signup.SignUpResult

data class SignUpForm(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
) {
    fun toRequest() = SignUpRequest(firstName, lastName, email, phone, password)
}

sealed interface SignUpUiState {
    /** What the user typed, kept in every state so the form is never emptied. */
    val form: SignUpForm

    data class Loading(override val form: SignUpForm) : SignUpUiState

    data class Success(
        override val form: SignUpForm = SignUpForm(),
        /** Null while the user is filling the form; set once the account is created. */
        val result: SignUpResult? = null,
    ) : SignUpUiState

    data class Error(
        override val form: SignUpForm,
        val fieldErrors: Set<SignUpFieldError> = emptySet(),
        /** Error not tied to a field (network, email already used…). */
        @StringRes val messageRes: Int? = null,
    ) : SignUpUiState
}
