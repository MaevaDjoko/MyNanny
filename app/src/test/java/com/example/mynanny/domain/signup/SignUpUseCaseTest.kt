package com.example.mynanny.domain.signup

import com.example.mynanny.domain.common.AuthException
import com.example.mynanny.testing.FakeSignUpRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Test

class SignUpUseCaseTest {

    private val repository = FakeSignUpRepository()
    private val signUp = SignUpUseCase(ValidateSignUpUseCase(), repository)

    @Test
    fun `cleans the form before creating the account`() = runTest {
        val result = signUp(SignUpRequest(" Marie ", " Dupont", " Marie@Mail.FR ", "06 12 34 56 78", "motdepasse"))

        assertEquals(SignUpResult.SIGNED_IN, result)
        assertEquals(
            SignUpRequest("Marie", "Dupont", "marie@mail.fr", "0612345678", "motdepasse"),
            repository.lastSignUpRequest,
        )
    }

    @Test
    fun `invalid form is rejected without calling the server`() = runTest {
        try {
            signUp(SignUpRequest("", "Dupont", "marie@mail.fr", "", "motdepasse"))
            fail("InvalidForm expected")
        } catch (e: AuthException.InvalidForm) {
            assertEquals(setOf(SignUpFieldError.FIRST_NAME_REQUIRED), e.errors)
        }
        assertNull(repository.lastSignUpRequest)
    }
}
