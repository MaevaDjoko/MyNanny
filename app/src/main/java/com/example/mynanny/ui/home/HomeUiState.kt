package com.example.mynanny.ui.home

import androidx.annotation.StringRes

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val firstName: String,
        /** Set after a successful sign out: the screen navigates back to the welcome screen. */
        val isSignedOut: Boolean = false,
    ) : HomeUiState

    data class Error(@StringRes val messageRes: Int) : HomeUiState
}
