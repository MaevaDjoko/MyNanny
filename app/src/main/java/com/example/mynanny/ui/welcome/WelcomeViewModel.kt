package com.example.mynanny.ui.welcome

import androidx.lifecycle.ViewModel
import com.example.mynanny.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<WelcomeUiState>(WelcomeUiState.Success())
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()

    fun onLocationChange(value: String) {
        _uiState.value = WelcomeUiState.Success(location = value)
    }

    fun onSearchClick() {
        val location = _uiState.value.location
        val trimmed = location.trim()
        _uiState.value = if (trimmed.length < MIN_LOCATION_LENGTH) {
            WelcomeUiState.Error(location, R.string.welcome_search_error)
        } else {
            WelcomeUiState.Success(location, searchLocation = trimmed)
        }
    }

    fun onSearchNavigated() {
        _uiState.value = WelcomeUiState.Success(location = _uiState.value.location)
    }

    companion object {
        const val MIN_LOCATION_LENGTH = 2
    }
}
