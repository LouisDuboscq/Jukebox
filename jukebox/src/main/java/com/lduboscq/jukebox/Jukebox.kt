package com.lduboscq.jukebox

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lduboscq.jukebox.defaultviews.AudioLoading
import com.lduboscq.jukebox.defaultviews.DefaultAudioSliderView
import com.lduboscq.jukebox.defaultviews.FileNotFoundText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun Jukebox(
    uri: Uri,
    modifier: Modifier = Modifier,
    onAudioListened: () -> Unit = { Log.d("AudioSlider", "onAudioListened") },
    onFileNotFound: () -> Unit = { Log.d("AudioSlider", "onFileNotFound") },
    commands: Flow<JukeboxViewModel.Commands>,
    loadingView: @Composable () -> Unit = { AudioLoading() },
    errorView: @Composable () -> Unit = { FileNotFoundText() },
    audioView: @Composable (JukeboxViewModel.State.Loaded, (Float) -> Unit) -> Unit =
        { state, onSeek -> DefaultAudioSliderView(state, onSeek) },
) {
    val viewModel: JukeboxViewModel = viewModel()

    LaunchedEffect(uri) { viewModel.initUri(uri) }

    LaunchedEffect(uri) {
        viewModel.effect.onEach {
            when (it) {
                JukeboxViewModel.Effect.AudioListened -> onAudioListened()
                JukeboxViewModel.Effect.FileNotFound -> onFileNotFound()
            }
        }.collect()
    }

    LaunchedEffect(uri) {
        commands.onEach {
            when (it) {
                JukeboxViewModel.Commands.Pause -> viewModel.pause()
                JukeboxViewModel.Commands.Play -> viewModel.play()
            }
        }.collect()
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    JukeboxContent(
        state = state,
        onSeek = viewModel::onSeek,
        modifier = modifier,
        loadingView = loadingView,
        errorView = errorView,
        audioView = audioView,
    )
}

@Composable
internal fun JukeboxContent(
    state: JukeboxViewModel.State,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
    loadingView: @Composable () -> Unit,
    errorView: @Composable () -> Unit,
    audioView: @Composable (JukeboxViewModel.State.Loaded, (Float) -> Unit) -> Unit
) {
    Box(modifier) {
        when (state) {
            is JukeboxViewModel.State.Loaded -> audioView(state, onSeek)
            JukeboxViewModel.State.Loading -> loadingView()
            JukeboxViewModel.State.FileNotFound -> errorView()
        }
    }
}
