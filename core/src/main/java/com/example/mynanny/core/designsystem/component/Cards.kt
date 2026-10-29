package com.example.mynanny.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mynanny.core.designsystem.theme.MyNannyTheme

/** Standard card for a list item or a block of information. Clickable when [onClick] is set. */
@Composable
fun MyNannyCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    val inner: @Composable ColumnScope.() -> Unit = {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = content,
        )
    }
    if (onClick != null) {
        Card(onClick = onClick, modifier = modifier, shape = MaterialTheme.shapes.large, colors = colors, content = inner)
    } else {
        Card(modifier = modifier, shape = MaterialTheme.shapes.large, colors = colors, content = inner)
    }
}

/** Eye-catching card with the brand gradient, for a hero or a key message. */
@Composable
fun MyNannyHighlightCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    listOf(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.secondaryContainer)
                ),
                shape = MaterialTheme.shapes.extraLarge,
            )
            .padding(24.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp), content = content)
    }
}

@Preview(showBackground = true)
@Composable
private fun CardsPreview() {
    MyNannyTheme {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            MyNannyHighlightCard {
                Text("Trouvez la nounou idéale", style = MaterialTheme.typography.headlineSmall)
            }
            MyNannyCard(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                Text("Garde mardi soir", style = MaterialTheme.typography.titleMedium)
                Text("18:00 → 21:00 · Lyon 3e", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
