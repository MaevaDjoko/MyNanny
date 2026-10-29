package com.example.mynanny.ui.signup

import com.example.mynanny.R
import com.example.mynanny.domain.common.AuthException
import com.example.mynanny.domain.signup.SignUpFieldError
import com.example.mynanny.domain.signup.SignUpResult
import com.example.mynanny.domain.signup.SignUpUseCase
import com.example.mynanny.domain.signup.ValidateSignUpUseCase
import com.example.mynanny.testing.FakeSignUpRepository
import com.example.mynanny.testing.MainDispatcherRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SignUpViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeSignUpRepository()
    private val viewModel = SignUpViewModel(SignUpUseCase(ValidateSignUpUseCase(), repository))

    private fun fillValidForm() {
        viewModel.onFirstNameChange("Marie")
        viewModel.onLastNameChange("Dupont")
        viewModel.onEmailChange("marie@mail.fr")
        viewModel.onPasswordChange("motdepasse")
    }

    @Test
    fun `valid form creates the account`() {
        fillValidForm()
        viewModel.onSubmit()
        assertEquals(SignUpResult.SIGNED_IN, (viewModel.uiState.value as SignUpUiState.Success).result)
    }

    @Test
    fun `email confirmation result is exposed to the screen`() {
        repository.signUpResult = SignUpResult.EMAIL_CONFIRMATION_REQUIRED
        fillValidForm()
        viewModel.onSubmit()
        assertEquals(
            SignUpResult.EMAIL_CONFIRMATION_REQUIRED,
            (viewModel.uiState.value as SignUpUiState.Success).result,
        )
    }

    @Test
    fun `invalid fields are shown and the typed values are kept`() {
        viewModel.onFirstNameChange("Marie")
        viewModel.onSubmit()

        val state = viewModel.uiState.value as SignUpUiState.Error
        assertEquals("Marie", state.form.firstName)
        assertEquals(
            setOf(
                SignUpFieldError.LAST_NAME_REQUIRED,
                SignUpFieldError.EMAIL_INVALID,
                SignUpFieldError.PASSWORD_TOO_SHORT,
            ),
            state.fieldErrors,
        )
    }

    @Test
    fun `editing a field only clears its own error`() {
        viewModel.onSubmit()
        viewModel.onEmailChange("marie@mail.fr")

        val state = viewModel.uiState.value as SignUpUiState.Error
        assertEquals(false, SignUpFieldError.EMAIL_INVALID in state.fieldErrors)
        assertEquals(true, SignUpFieldError.FIRST_NAME_REQUIRED in state.fieldErrors)
    }

    @Test
    fun `server errors are translated into messages`() {
        repository.signUpError = AuthException.EmailAlreadyUsed()
        fillValidForm()
        viewModel.onSubmit()
        assertEquals(
            R.string.sign_up_error_email_used,
            (viewModel.uiState.value as SignUpUiState.Error).messageRes,
        )
    }
}
