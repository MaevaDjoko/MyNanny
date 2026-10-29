package com.example.mynanny.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mynanny.core.R
import com.example.mynanny.core.designsystem.theme.MyNannyTheme

enum class LoaderSize(val diameter: Dp, val strokeWidth: Dp) {
    /** Inside a button or a text field. */
    Small(20.dp, 2.dp),

    /** Alone in a screen or a section. */
    Large(40.dp, 4.dp),
}

@Composable
fun MyNannyLoader(
    modifier: Modifier = Modifier,
    size: LoaderSize = LoaderSize.Large,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    val description = stringResource(R.string.core_loading)
    CircularProgressIndicator(
        color = color,
        strokeWidth = size.strokeWidth,
        modifier = modifier
            .size(size.diameter)
            .semantics { contentDescription = description },
    )
}

/** Loader centred in all the available space, for a screen that is loading. */
@Composable
fun MyNannyFullScreenLoader(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        MyNannyLoader()
    }
}

@Preview(showBackground = true, widthDp = 200, heightDp = 200)
@Composable
private fun LoaderPreview() {
    MyNannyTheme { MyNannyFullScreenLoader() }
}
