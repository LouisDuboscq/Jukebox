package com.lduboscq.jukeboxe.examples

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lduboscq.jukebox.Jukebox
import com.lduboscq.jukebox.JukeboxViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Jukebox component with play and pause buttons
 */
@Composable
fun JukeboxSample(uri: Uri, setNextUri: () -> Unit) {
    val commands: Channel<JukeboxViewModel.Commands> = Channel()
    val commandsScope = rememberCoroutineScope()

    Column(
        Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        verticalArrangement = Arrangement.Center
    ) {
        Jukebox(
            uri = uri,
            commands = commands.receiveAsFlow(), // user clicks for play and pause
            onAudioListened = setNextUri, // in this sample, onAudioListened is just change uri to next mp3
            modifier = Modifier.padding(horizontal = 32.dp)
        )
        Spacer(Modifier.height(16.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                commandsScope.launch {
                    commands.send(JukeboxViewModel.Commands.Play)
                }
            }) {
                Text("play")
            }
            Spacer(Modifier.width(16.dp))
            Button(onClick = {
                commandsScope.launch {
                    commands.send(JukeboxViewModel.Commands.Pause)
                }
            }) {
                Text("pause")
            }
        }
    }
}
