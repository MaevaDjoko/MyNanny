package com.example.mynanny.ui.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mynanny.R
import com.example.mynanny.core.designsystem.component.MyNannyPasswordField
import com.example.mynanny.core.designsystem.component.MyNannyPrimaryButton
import com.example.mynanny.core.designsystem.component.MyNannyTextButton
import com.example.mynanny.core.designsystem.component.MyNannyTextField
import com.example.mynanny.core.designsystem.theme.MyNannyTheme
import com.example.mynanny.domain.signup.SignUpFieldError
import com.example.mynanny.domain.signup.SignUpResult
import com.example.mynanny.domain.signup.ValidateSignUpUseCase

/** Stateful entry point: wires the ViewModel to the stateless [SignUpScreen]. */
@Composable
fun SignUpRoute(
    onBack: () -> Unit,
    onSignedUp: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val result = (uiState as? SignUpUiState.Success)?.result
    LaunchedEffect(result) {
        if (result == SignUpResult.SIGNED_IN) onSignedUp()
    }

    SignUpScreen(
        uiState = uiState,
        onBack = onBack,
        onFirstNameChange = viewModel::onFirstNameChange,
        onLastNameChange = viewModel::onLastNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPhoneChange = viewModel::onPhoneChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSubmit = viewModel::onSubmit,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    uiState: SignUpUiState,
    onBack: () -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.sign_up_title)) },
                navigationIcon = { MyNannyTextButton(text = stringResource(R.string.action_back), onClick = onBack) },
            )
        },
    ) { padding ->
        val confirmationRequired =
            (uiState as? SignUpUiState.Success)?.result == SignUpResult.EMAIL_CONFIRMATION_REQUIRED
        if (confirmationRequired) {
            EmailConfirmationContent(email = uiState.form.email, onBack = onBack, modifier = Modifier.padding(padding))
        } else {
            SignUpForm(
                uiState = uiState,
                onFirstNameChange = onFirstNameChange,
                onLastNameChange = onLastNameChange,
                onEmailChange = onEmailChange,
                onPhoneChange = onPhoneChange,
                onPasswordChange = onPasswordChange,
                onSubmit = onSubmit,
                modifier = Modifier.padding(padding),
            )
        }
    }
}

@Composable
private fun SignUpForm(
    uiState: SignUpUiState,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val form = uiState.form
    val isLoading = uiState is SignUpUiState.Loading
    val error = uiState as? SignUpUiState.Error
    val fieldErrors = error?.fieldErrors.orEmpty()

    @Composable
    fun errorFor(field: SignUpFieldError): String? = if (field in fieldErrors) fieldErrorText(field) else null

    val nameKeyboard = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next)

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.sign_up_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MyNannyTextField(
                value = form.firstName,
                onValueChange = onFirstNameChange,
                label = stringResource(R.string.sign_up_first_name),
                errorMessage = errorFor(SignUpFieldError.FIRST_NAME_REQUIRED),
                enabled = !isLoading,
                keyboardOptions = nameKeyboard,
                modifier = Modifier.weight(1f),
            )
            MyNannyTextField(
                value = form.lastName,
                onValueChange = onLastNameChange,
                label = stringResource(R.string.sign_up_last_name),
                errorMessage = errorFor(SignUpFieldError.LAST_NAME_REQUIRED),
                enabled = !isLoading,
                keyboardOptions = nameKeyboard,
                modifier = Modifier.weight(1f),
            )
        }
        MyNannyTextField(
            value = form.email,
            onValueChange = onEmailChange,
            label = stringResource(R.string.sign_up_email),
            errorMessage = errorFor(SignUpFieldError.EMAIL_INVALID),
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
        )
        MyNannyTextField(
            value = form.phone,
            onValueChange = onPhoneChange,
            label = stringResource(R.string.sign_up_phone),
            errorMessage = errorFor(SignUpFieldError.PHONE_INVALID),
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
        )
        MyNannyPasswordField(
            value = form.password,
            onValueChange = onPasswordChange,
            label = stringResource(R.string.sign_up_password),
            helperText = stringResource(R.string.sign_up_password_hint),
            errorMessage = errorFor(SignUpFieldError.PASSWORD_TOO_SHORT),
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onSubmit() }),
            modifier = Modifier.fillMaxWidth(),
        )

        error?.messageRes?.let {
            Text(stringResource(it), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        }

        MyNannyPrimaryButton(
            text = stringResource(R.string.sign_up_submit),
            onClick = onSubmit,
            isLoading = isLoading,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun fieldErrorText(error: SignUpFieldError): String = when (error) {
    SignUpFieldError.FIRST_NAME_REQUIRED, SignUpFieldError.LAST_NAME_REQUIRED -> stringResource(R.string.sign_up_error_required)
    SignUpFieldError.EMAIL_INVALID -> stringResource(R.string.sign_up_error_email)
    SignUpFieldError.PHONE_INVALID -> stringResource(R.string.sign_up_error_phone)
    SignUpFieldError.PASSWORD_TOO_SHORT ->
        stringResource(R.string.sign_up_error_password, ValidateSignUpUseCase.MIN_PASSWORD_LENGTH)
}

@Composable
private fun EmailConfirmationContent(email: String, onBack: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("✉️", style = MaterialTheme.typography.displayMedium)
        Text(
            text = stringResource(R.string.sign_up_confirm_title),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(R.string.sign_up_confirm_message, email.trim()),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        MyNannyPrimaryButton(text = stringResource(R.string.sign_up_confirm_back), onClick = onBack)
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpScreenErrorPreview() {
    MyNannyTheme {
        SignUpScreen(
            uiState = SignUpUiState.Error(
                form = SignUpForm(firstName = "Marie", email = "marie@"),
                fieldErrors = setOf(SignUpFieldError.LAST_NAME_REQUIRED, SignUpFieldError.EMAIL_INVALID),
            ),
            onBack = {}, onFirstNameChange = {}, onLastNameChange = {}, onEmailChange = {},
            onPhoneChange = {}, onPasswordChange = {}, onSubmit = {},
        )
    }
}
