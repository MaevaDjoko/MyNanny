package com.example.mynanny.ui.splash

sealed interface SplashUiState {
    data object Loading : SplashUiState
    data class Success(val isLoggedIn: Boolean) : SplashUiState
    data class Error(val message: String) : SplashUiState
}
