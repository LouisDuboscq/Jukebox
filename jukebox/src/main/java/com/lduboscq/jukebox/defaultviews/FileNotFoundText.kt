package com.lduboscq.jukebox.defaultviews

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun FileNotFoundText(modifier: Modifier = Modifier) {
    Text(
        "File not found",
        color = Color.Red,
        modifier = modifier
    )
}
