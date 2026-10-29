package com.example.mynanny.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynanny.R
import com.example.mynanny.domain.common.AuthException
import com.example.mynanny.domain.signup.SignUpFieldError
import com.example.mynanny.domain.signup.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUp: SignUpUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Success())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun onFirstNameChange(value: String) =
        updateForm(SignUpFieldError.FIRST_NAME_REQUIRED) { copy(firstName = value) }

    fun onLastNameChange(value: String) =
        updateForm(SignUpFieldError.LAST_NAME_REQUIRED) { copy(lastName = value) }

    fun onEmailChange(value: String) =
        updateForm(SignUpFieldError.EMAIL_INVALID) { copy(email = value) }

    fun onPhoneChange(value: String) =
        updateForm(SignUpFieldError.PHONE_INVALID) { copy(phone = value) }

    fun onPasswordChange(value: String) =
        updateForm(SignUpFieldError.PASSWORD_TOO_SHORT) { copy(password = value) }

    fun onSubmit() {
        val state = _uiState.value
        if (state is SignUpUiState.Loading) return
        viewModelScope.launch {
            _uiState.value = SignUpUiState.Loading(state.form)
            _uiState.value = try {
                SignUpUiState.Success(state.form, result = signUp(state.form.toRequest()))
            } catch (e: AuthException) {
                e.toErrorState(state.form)
            }
        }
    }

    /** Applies the edit and removes the error of the edited field only; other errors stay visible. */
    private fun updateForm(editedField: SignUpFieldError, edit: SignUpForm.() -> SignUpForm) {
        val state = _uiState.value
        if (state is SignUpUiState.Loading) return
        val form = state.form.edit()
        val remainingErrors = (state as? SignUpUiState.Error)?.fieldErrors.orEmpty() - editedField
        _uiState.value =
            if (remainingErrors.isEmpty()) SignUpUiState.Success(form)
            else SignUpUiState.Error(form, remainingErrors)
    }

    private fun AuthException.toErrorState(form: SignUpForm): SignUpUiState.Error = when (this) {
        is AuthException.InvalidForm -> SignUpUiState.Error(form, fieldErrors = errors)
        is AuthException.EmailAlreadyUsed -> SignUpUiState.Error(form, messageRes = R.string.sign_up_error_email_used)
        is AuthException.InvalidEmail ->
            SignUpUiState.Error(form, fieldErrors = setOf(SignUpFieldError.EMAIL_INVALID))
        is AuthException.WeakPassword -> SignUpUiState.Error(form, messageRes = R.string.sign_up_error_weak_password)
        is AuthException.TooManyAttempts -> SignUpUiState.Error(form, messageRes = R.string.error_too_many_attempts)
        is AuthException.Network -> SignUpUiState.Error(form, messageRes = R.string.error_network)
        is AuthException.Unknown -> SignUpUiState.Error(form, messageRes = R.string.error_unknown)
    }
}
