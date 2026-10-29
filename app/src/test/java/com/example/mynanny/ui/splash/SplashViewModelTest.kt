package com.example.mynanny.ui.splash

import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.example.mynanny.domain.session.SessionStatus
import com.example.mynanny.domain.session.ObserveSessionStatusUseCase
import com.example.mynanny.testing.FakeSessionRepository
import com.example.mynanny.testing.MainDispatcherRule
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeSessionRepository()

    private fun createViewModel() = SplashViewModel(ObserveSessionStatusUseCase(repository))

    @Test
    fun `stays loading until the session is restored`() {
        val viewModel = createViewModel()
        assertEquals(SplashUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `ready and logged in when a session exists`() = runTest {
        val viewModel = createViewModel()
        repository.sessionStatus.emit(SessionStatus.Authenticated("user-1"))
        assertEquals(SplashUiState.Success(isLoggedIn = true), viewModel.uiState.value)
    }

    @Test
    fun `ready and logged out when there is no session`() = runTest {
        val viewModel = createViewModel()
        repository.sessionStatus.emit(SessionStatus.Unauthenticated)
        assertEquals(SplashUiState.Success(isLoggedIn = false), viewModel.uiState.value)
    }

    @Test
    fun `does not block the app forever when the session never comes`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel()
        advanceTimeBy(3_001)
        assertTrue(viewModel.uiState.value is SplashUiState.Error)
    }
}
