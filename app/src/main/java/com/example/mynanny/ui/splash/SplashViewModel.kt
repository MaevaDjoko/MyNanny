package com.example.mynanny.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynanny.domain.session.SessionStatus
import com.example.mynanny.domain.session.ObserveSessionStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

/** Keeps the system splash screen visible until the saved session has been restored. */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val observeSessionStatus: ObserveSessionStatusUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            restoreSession()
        }
    }

    private suspend fun restoreSession() {
        _uiState.value = SplashUiState.Loading
        _uiState.value = try {
            val status = withTimeout(SESSION_RESTORE_TIMEOUT_MS) { observeSessionStatus().first() }
            SplashUiState.Success(isLoggedIn = status is SessionStatus.Authenticated)
        } catch (e: TimeoutCancellationException) {
            // Never block the user on the splash: the app continues as logged out.
            SplashUiState.Error("Impossible de restaurer la session")
        }
    }

    private companion object {
        const val SESSION_RESTORE_TIMEOUT_MS = 3_000L
    }
}
