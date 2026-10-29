package com.example.mynanny.ui.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mynanny.R
import com.example.mynanny.core.designsystem.component.MyNannyHighlightCard
import com.example.mynanny.core.designsystem.component.MyNannyPrimaryButton
import com.example.mynanny.core.designsystem.component.MyNannySecondaryButton
import com.example.mynanny.core.designsystem.component.MyNannyTextButton
import com.example.mynanny.core.designsystem.component.MyNannyTextField
import com.example.mynanny.core.designsystem.theme.MyNannyTheme

/** Stateful entry point: wires the ViewModel to the stateless [WelcomeScreen]. */
@Composable
fun WelcomeRoute(
    onSearchNannies: (location: String) -> Unit,
    onLogin: () -> Unit,
    onSignUp: () -> Unit,
    viewModel: WelcomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // The search is validated by the ViewModel; navigate once it's accepted, then reset the state.
    val searchLocation = (uiState as? WelcomeUiState.Success)?.searchLocation
    LaunchedEffect(searchLocation) {
        searchLocation?.let { location ->
            onSearchNannies(location)
            viewModel.onSearchNavigated()
        }
    }

    WelcomeScreen(
        uiState = uiState,
        onLocationChange = viewModel::onLocationChange,
        onSearchClick = viewModel::onSearchClick,
        onLoginClick = onLogin,
        onSignUpClick = onSignUp,
    )
}

@Composable
fun WelcomeScreen(
    uiState: WelcomeUiState,
    onLocationChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding()
            .imePadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
    ) {
        Hero()
        Spacer(Modifier.height(28.dp))
        SearchSection(uiState, onLocationChange, onSearchClick)
        Spacer(Modifier.height(32.dp))
        AuthSection(onLoginClick, onSignUpClick)
    }
}

@Composable
private fun Hero() {
    MyNannyHighlightCard {
        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surface, modifier = Modifier.size(64.dp)) {
            Box(contentAlignment = Alignment.Center) { Text("👶", fontSize = 32.sp) }
        }
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Text(
            text = stringResource(R.string.welcome_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Benefit("✓", stringResource(R.string.welcome_benefit_profiles))
        Benefit("📍", stringResource(R.string.welcome_benefit_tracking))
        Benefit("📅", stringResource(R.string.welcome_benefit_booking))
    }
}

@Composable
private fun Benefit(symbol: String, text: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(symbol, modifier = Modifier.size(24.dp), textAlign = TextAlign.Center)
        Text(text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}

@Composable
private fun SearchSection(
    uiState: WelcomeUiState,
    onLocationChange: (String) -> Unit,
    onSearchClick: () -> Unit,
) {
    val isLoading = uiState is WelcomeUiState.Loading
    val errorMessage = (uiState as? WelcomeUiState.Error)?.let {
        stringResource(it.messageRes, WelcomeViewModel.MIN_LOCATION_LENGTH)
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = stringResource(R.string.welcome_search_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )
        MyNannyTextField(
            value = uiState.location,
            onValueChange = onLocationChange,
            label = stringResource(R.string.welcome_search_label),
            placeholder = stringResource(R.string.welcome_search_placeholder),
            errorMessage = errorMessage,
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearchClick() }),
            modifier = Modifier.fillMaxWidth(),
        )
        MyNannyPrimaryButton(
            text = stringResource(R.string.welcome_search_button),
            onClick = onSearchClick,
            isLoading = isLoading,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun AuthSection(onLoginClick: () -> Unit, onSignUpClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(Modifier.weight(1f))
            Text(
                text = stringResource(R.string.welcome_auth_divider),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 12.dp),
            )
            HorizontalDivider(Modifier.weight(1f))
        }
        MyNannySecondaryButton(
            text = stringResource(R.string.welcome_login),
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth(),
        )
        MyNannyTextButton(text = stringResource(R.string.welcome_sign_up), onClick = onSignUpClick)
    }
}

@Preview(showBackground = true)
@Composable
private fun WelcomeScreenPreview() {
    MyNannyTheme {
        WelcomeScreen(
            uiState = WelcomeUiState.Success(),
            onLocationChange = {},
            onSearchClick = {},
            onLoginClick = {},
            onSignUpClick = {},
        )
    }
}
