package com.example.mynanny.ui.welcome

import com.example.mynanny.R
import org.junit.Assert.assertEquals
import org.junit.Test

class WelcomeViewModelTest {

    private val viewModel = WelcomeViewModel()

    @Test
    fun `starts in success with an empty search`() {
        assertEquals(WelcomeUiState.Success(), viewModel.uiState.value)
    }

    @Test
    fun `search with a too short location gives an error and keeps the typed text`() {
        viewModel.onLocationChange(" L ")
        viewModel.onSearchClick()
        assertEquals(WelcomeUiState.Error(" L ", R.string.welcome_search_error), viewModel.uiState.value)
    }

    @Test
    fun `typing again clears the error`() {
        viewModel.onSearchClick()
        viewModel.onLocationChange("Ly")
        assertEquals(WelcomeUiState.Success(location = "Ly"), viewModel.uiState.value)
    }

    @Test
    fun `search with a valid location requests navigation with the trimmed value`() {
        viewModel.onLocationChange("  Lyon ")
        viewModel.onSearchClick()
        assertEquals(WelcomeUiState.Success("  Lyon ", searchLocation = "Lyon"), viewModel.uiState.value)
    }

    @Test
    fun `navigation request is cleared once handled`() {
        viewModel.onLocationChange("Lyon")
        viewModel.onSearchClick()
        viewModel.onSearchNavigated()
        assertEquals(WelcomeUiState.Success(location = "Lyon"), viewModel.uiState.value)
    }
}
