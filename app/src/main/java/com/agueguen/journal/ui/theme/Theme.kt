package com.agueguen.journal.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun JournalTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content
    )
}