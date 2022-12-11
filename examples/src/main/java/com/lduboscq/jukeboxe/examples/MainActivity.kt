package com.lduboscq.jukeboxe.examples

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var itemPlaying by remember { mutableStateOf(0) }
            var uri by remember { mutableStateOf(Uri.parse("https://filesamples.com/samples/audio/mp3/sample1.mp3")) }

            JukeboxSample(
                uri,
                setNextUri = {
                    if (itemPlaying < 3) itemPlaying += 1
                    uri = Uri.parse(
                        when (itemPlaying) {
                            0 -> "https://filesamples.com/samples/audio/mp3/sample1.mp3"
                            1 -> "https://filesamples.com/samples/audio/mp3/sample2.mp3"
                            2 -> "https://filesamples.com/samples/audio/mp3/sample3.mp3"
                            3 -> "https://filesamples.com/samples/audio/mp3/sample4.mp3"
                            else -> throw IllegalAccessError()
                        }
                    )
                    Log.d("MainActivity", "onAudioListened $itemPlaying $uri")
                })
        }
    }
}
