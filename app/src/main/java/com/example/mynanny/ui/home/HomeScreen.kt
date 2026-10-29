package com.example.mynanny.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mynanny.R
import com.example.mynanny.core.designsystem.component.MyNannyLoader
import com.example.mynanny.core.designsystem.component.MyNannySecondaryButton
import com.example.mynanny.core.designsystem.theme.MyNannyTheme

@Composable
fun HomeRoute(
    onSignedOut: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isSignedOut = (uiState as? HomeUiState.Success)?.isSignedOut == true
    LaunchedEffect(isSignedOut) {
        if (isSignedOut) onSignedOut()
    }

    HomeScreen(uiState = uiState, onSignOutClick = viewModel::onSignOutClick)
}

/** Temporary home: confirms the account works. The real home (search, bookings…) comes next. */
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().safeDrawingPadding().padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (uiState) {
            is HomeUiState.Loading -> MyNannyLoader()
            is HomeUiState.Error -> Text(
                text = stringResource(uiState.messageRes),
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
            )
            is HomeUiState.Success -> {
                Text("👋", style = MaterialTheme.typography.displayMedium)
                Text(
                    text = stringResource(R.string.home_greeting, uiState.firstName),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = stringResource(R.string.home_account_ready),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
            }
        }
        if (uiState !is HomeUiState.Loading) {
            MyNannySecondaryButton(text = stringResource(R.string.home_sign_out), onClick = onSignOutClick)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    MyNannyTheme {
        HomeScreen(uiState = HomeUiState.Success(firstName = "Marie"), onSignOutClick = {})
    }
}
