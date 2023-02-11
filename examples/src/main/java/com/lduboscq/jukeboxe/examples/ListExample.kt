package com.lduboscq.jukeboxe.examples

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.PlayArrow
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

@Composable
fun ListExample() {

    val commands1: Channel<JukeboxViewModel.Commands> = Channel()
    val commands2: Channel<JukeboxViewModel.Commands> = Channel()
    val commands3: Channel<JukeboxViewModel.Commands> = Channel()

    Column {
        ListenAudioItem(
            "Audio 1",
            Uri.parse("https://filesamples.com/samples/audio/mp3/sample1.mp3"),
            commands1
        )
        Spacer(Modifier.height(16.dp))

        ListenAudioItem(
            "Audio 2",
            Uri.parse("https://filesamples.com/samples/audio/mp3/sample2.mp3"),
            commands2
        )

        Spacer(Modifier.height(16.dp))

        ListenAudioItem(
            "Audio 3",
            Uri.parse("https://filesamples.com/samples/audio/mp3/sample3.mp3"),
            commands3
        )
    }
}


@Composable
private fun ListenAudioItem(
    name: String,
    uri: Uri,
    flowCommands: Channel<JukeboxViewModel.Commands>,
) {
    val commandsScope = rememberCoroutineScope()

    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(
                name,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(8.dp))

            Jukebox(
                uri = uri,
                commands = flowCommands.receiveAsFlow(),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            ) {
                TextButton(
                    onClick = {
                        commandsScope.launch { flowCommands.send(JukeboxViewModel.Commands.Pause) }
                    },
                    modifier = Modifier,
                ) {
                    Text(text = "Pause")
                }
                Spacer(Modifier.width(8.dp))
                TextButton(
                    modifier = Modifier,
                    onClick = {
                        commandsScope.launch { flowCommands.send(JukeboxViewModel.Commands.Play) }
                    },
                ) { Text("Play") }
            }
        }
    }
}
