package com.example.mynanny.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mynanny.core.designsystem.theme.MyNannyTheme

private val ButtonMinHeight = 52.dp

/** Main action of a screen (one per screen). Shows a loader and is disabled while [isLoading]. */
@Composable
fun MyNannyPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.heightIn(min = ButtonMinHeight),
    ) {
        if (isLoading) MyNannyLoader(size = LoaderSize.Small, color = MaterialTheme.colorScheme.onPrimary)
        else Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

/** Alternative action, less prominent than [MyNannyPrimaryButton]. */
@Composable
fun MyNannySecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.heightIn(min = ButtonMinHeight),
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

/** Low-emphasis action, e.g. links ("Back", "Create an account"). */
@Composable
fun MyNannyTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    TextButton(onClick = onClick, enabled = enabled, modifier = modifier) {
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonsPreview() {
    MyNannyTheme {
        Column(Modifier.padding(16.dp)) {
            MyNannyPrimaryButton("Créer mon compte", onClick = {}, modifier = Modifier.fillMaxWidth())
            MyNannyPrimaryButton("Créer mon compte", onClick = {}, isLoading = true, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
            MyNannySecondaryButton("Se connecter", onClick = {}, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
            MyNannyTextButton("Nouveau ? Créer un compte", onClick = {})
        }
    }
}
