package com.example.mynanny.ui.home

import com.example.mynanny.R
import com.example.mynanny.domain.common.AuthException
import com.example.mynanny.domain.session.ObserveCurrentUserUseCase
import com.example.mynanny.domain.session.RefreshCurrentUserUseCase
import com.example.mynanny.domain.session.SignOutUseCase
import com.example.mynanny.domain.session.User
import com.example.mynanny.testing.FakeSessionRepository
import com.example.mynanny.testing.MainDispatcherRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeSessionRepository()
    private val marie = User("id-1", "marie@mail.fr", "Marie", "Dupont", "")

    private fun createViewModel() = HomeViewModel(
        ObserveCurrentUserUseCase(repository),
        RefreshCurrentUserUseCase(repository),
        SignOutUseCase(repository),
    )

    @Test
    fun `shows the profile loaded from the server`() {
        repository.serverUser = marie
        val viewModel = createViewModel()
        assertEquals(HomeUiState.Success(firstName = "Marie"), viewModel.uiState.value)
    }

    @Test
    fun `offline with a cached profile shows the cache without error`() {
        repository.cachedUser.value = marie
        repository.refreshError = AuthException.Network(RuntimeException())
        val viewModel = createViewModel()
        assertEquals(HomeUiState.Success(firstName = "Marie"), viewModel.uiState.value)
    }

    @Test
    fun `offline without cache shows a network error`() {
        repository.refreshError = AuthException.Network(RuntimeException())
        val viewModel = createViewModel()
        assertEquals(HomeUiState.Error(R.string.error_network), viewModel.uiState.value)
    }

    @Test
    fun `sign out clears the cache and requests navigation`() {
        repository.serverUser = marie
        val viewModel = createViewModel()

        viewModel.onSignOutClick()

        assertEquals(HomeUiState.Success(firstName = "Marie", isSignedOut = true), viewModel.uiState.value)
        assertEquals(null, repository.cachedUser.value)
    }
}
