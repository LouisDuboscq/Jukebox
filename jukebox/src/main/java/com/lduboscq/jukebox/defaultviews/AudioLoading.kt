package com.lduboscq.jukebox.defaultviews

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AudioLoading(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally))
}
