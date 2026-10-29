package com.example.mynanny.domain.session

import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.example.mynanny.testing.FakeSessionRepository
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ObserveSessionStatusUseCaseTest {

    private val repository = FakeSessionRepository()
    private val useCase = ObserveSessionStatusUseCase(repository)

    @Test
    fun `ignores repeated identical statuses`() = runTest {
        val received = mutableListOf<SessionStatus>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) { useCase().take(2).toList(received) }

        repository.sessionStatus.emit(SessionStatus.Unauthenticated)
        repository.sessionStatus.emit(SessionStatus.Unauthenticated)
        repository.sessionStatus.emit(SessionStatus.Authenticated("user-1"))
        job.join()

        assertEquals(listOf(SessionStatus.Unauthenticated, SessionStatus.Authenticated("user-1")), received)
    }
}
