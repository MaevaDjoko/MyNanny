package com.example.mynanny.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynanny.R
import com.example.mynanny.domain.common.AuthException
import com.example.mynanny.domain.session.ObserveCurrentUserUseCase
import com.example.mynanny.domain.session.RefreshCurrentUserUseCase
import com.example.mynanny.domain.session.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeCurrentUser: ObserveCurrentUserUseCase,
    private val refreshCurrentUser: RefreshCurrentUserUseCase,
    private val signOut: SignOutUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    /** While signing out, profile updates are ignored so the screen doesn't flash back to "Hello". */
    private var isSigningOut = false

    init {
        // Cached profile is shown right away, then replaced by the server version once refreshed.
        viewModelScope.launch {
            observeCurrentUser().collect { user ->
                if (user != null && !isSigningOut) {
                    _uiState.value = HomeUiState.Success(firstName = user.firstName)
                }
            }
        }
        viewModelScope.launch {
            refresh()
        }
    }

    private suspend fun refresh() {
        try {
            refreshCurrentUser()
        } catch (e: AuthException) {
            // Only an error if there is nothing to show: with a cached profile, stay on it silently.
            if (_uiState.value is HomeUiState.Loading) {
                _uiState.value = HomeUiState.Error(e.toMessageRes())
            }
        }
    }

    fun onSignOutClick() {
        val firstName = (_uiState.value as? HomeUiState.Success)?.firstName.orEmpty()
        isSigningOut = true
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            _uiState.value = try {
                signOut()
                HomeUiState.Success(firstName, isSignedOut = true)
            } catch (e: AuthException) {
                isSigningOut = false
                HomeUiState.Error(e.toMessageRes())
            }
        }
    }

    private fun AuthException.toMessageRes(): Int = when (this) {
        is AuthException.Network -> R.string.error_network
        is AuthException.TooManyAttempts -> R.string.error_too_many_attempts
        else -> R.string.error_unknown
    }
}
