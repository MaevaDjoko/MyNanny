package com.example.mynanny.ui.welcome

import androidx.annotation.StringRes

sealed interface WelcomeUiState {
    /** Text typed in the search field, kept in every state so it is never lost. */
    val location: String

    data class Loading(override val location: String = "") : WelcomeUiState

    data class Success(
        override val location: String = "",
        /** Set when the search is valid: the screen navigates, then calls onSearchNavigated(). */
        val searchLocation: String? = null,
    ) : WelcomeUiState

    data class Error(
        override val location: String,
        @StringRes val messageRes: Int,
    ) : WelcomeUiState
}
