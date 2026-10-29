package com.example.mynanny.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mynanny.core.R
import com.example.mynanny.core.designsystem.theme.MyNannyTheme

/**
 * Single-line form field. [errorMessage] takes precedence over [helperText] and turns the field red.
 */
@Composable
fun MyNannyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    helperText: String? = null,
    errorMessage: String? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    val supporting = errorMessage ?: helperText
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = placeholder?.let { { Text(it) } },
        supportingText = supporting?.let { { Text(it) } },
        isError = errorMessage != null,
        enabled = enabled,
        singleLine = true,
        shape = MaterialTheme.shapes.medium,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        trailingIcon = trailingContent,
        modifier = modifier,
    )
}

/** Password field with a built-in "Show / Hide" toggle. */
@Composable
fun MyNannyPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    helperText: String? = null,
    errorMessage: String? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    MyNannyTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        helperText = helperText,
        errorMessage = errorMessage,
        enabled = enabled,
        keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Password),
        keyboardActions = keyboardActions,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingContent = {
            TextButton(onClick = { visible = !visible }, enabled = enabled) {
                Text(stringResource(if (visible) R.string.core_password_hide else R.string.core_password_show))
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun TextFieldsPreview() {
    MyNannyTheme {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            MyNannyTextField(value = "Marie", onValueChange = {}, label = "Prénom", modifier = Modifier.fillMaxWidth())
            MyNannyTextField(
                value = "marie@", onValueChange = {}, label = "Email",
                errorMessage = "Adresse email invalide", modifier = Modifier.fillMaxWidth(),
            )
            MyNannyPasswordField(
                value = "secret", onValueChange = {}, label = "Mot de passe",
                helperText = "8 caractères minimum", modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
