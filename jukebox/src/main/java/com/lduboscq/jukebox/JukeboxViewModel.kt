package com.lduboscq.jukebox

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.io.IOException

class JukeboxViewModel(private val playWhenReady: Boolean) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.None)
    val state = _state.asStateFlow()

    private val jukeboxPlayer = JukeboxPlayer()

    private var refreshJob: Job? = null

    private lateinit var uri: Uri

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    sealed interface Effect {
        object AudioListened : Effect
        object FileNotFound : Effect
    }

    sealed interface State {
        object None : State
        object Preparing : State
        object FileNotFound : State

        data class Loaded(
            val playingState: PlayingState = PlayingState.Pause,
            val mediaPlayerPosition: Int = 0,
            val mediaPlayerDuration: Int = 0,
        ) : State {
            val mediaPlayerPositionText: String = mediaPlayerPosition.toLong().toTimeText()
            val mediaPlayerDurationText: String = mediaPlayerDuration.toLong().toTimeText()
        }
    }

    sealed interface PlayingState {
        object Pause : PlayingState
        object Playing : PlayingState
    }

    var uris = mutableListOf<Uri>()
    fun initUri(uri: Uri) {
        this.uri = uri
        uris.add(uri)

        try {
            jukeboxPlayer.setDataSource(uri)
        } catch (e: IllegalStateException) {
            viewModelScope.launch { _effect.send(Effect.FileNotFound) }
            _state.value = State.FileNotFound
            return
        }

        jukeboxPlayer.prepare(uri) { player ->
            _state.value = State.Loaded(mediaPlayerDuration = player.duration)
        }

        Log.d("JukeboxViewModel", "uri : $uri")
    }

    private suspend fun prepareMediaPlayer(startOnPrepared: Boolean) {
        jukeboxPlayer.reset(uri)
        _state.value = State.Preparing
        try {
            jukeboxPlayer.setDataSource(uri)
        } catch (e: FileNotFoundException) {
            _effect.send(Effect.FileNotFound)
            _state.value = State.FileNotFound
            return
        } catch (e: IOException) {
            _effect.send(Effect.FileNotFound)
            _state.value = State.FileNotFound
            return
        }

        jukeboxPlayer.prepare(uri) { player ->
            _state.value = State.Loaded(mediaPlayerDuration = player.duration)
            /*TODO if (playWhenReady) {
                play()
            }*/

            if (startOnPrepared) {
                play()
            }
        }
        jukeboxPlayer.setOnCompletionListener(uri) {
            _state.value = State.Loaded(
                mediaPlayerDuration = it.duration,
                mediaPlayerPosition = 0,
                playingState = PlayingState.Pause
            )
            resetToStart()
            viewModelScope.launch { _effect.send(Effect.AudioListened) }
        }
    }

    /**
     * @throws IllegalStateException if play is not called in Loaded state
     */
    fun play() {
        fun startAndRefreshPosition() {
            refreshJob = viewModelScope.launch {
                while (true) {
                    if (_state.value is State.Loaded) {
                        _state.value = (_state.value as State.Loaded).copy(
                            mediaPlayerPosition = jukeboxPlayer.currentPosition(uri),
                        )
                    }
                    delay(1000)
                }
            }
            _state.value = (_state.value as State.Loaded).copy(playingState = PlayingState.Playing)
            jukeboxPlayer.start(uri)
        }

        if (_state.value is State.None) {
            viewModelScope.launch {
                prepareMediaPlayer(true)
            }
        } else {
            startAndRefreshPosition()
        }
    }

    fun pause() {
        if (_state.value is State.Loaded) {
            refreshJob?.cancel()
            try {
                jukeboxPlayer.pause(uri)
            } catch (e: IllegalStateException) {
                Log.e("ListenAudioViewModel", "pause $e")
            }
            _state.value = (_state.value as State.Loaded).copy(playingState = PlayingState.Pause)
        }
    }

    private fun resetToStart() {
        check(_state.value is State.Loaded)
        refreshJob?.cancel()
        jukeboxPlayer.seekTo(0, uri)
        _state.value = (_state.value as State.Loaded).copy(
            playingState = PlayingState.Pause,
            mediaPlayerPosition = 0,
        )
    }

    override fun onCleared() {
        super.onCleared()
        releaseMediaPlayer()
    }

    private fun releaseMediaPlayer() {
        jukeboxPlayer.stopAndRelease(uri)
    }

    fun onSeek(sliderPosition: Float) {
        if (_state.value is State.Loaded) {
            jukeboxPlayer.seekTo(sliderPosition.toInt(), uri)
            _state.value = (_state.value as State.Loaded).copy(
                mediaPlayerPosition = sliderPosition.toInt(),
            )
        }
    }

    sealed interface Commands {
        object Play : Commands
        object Pause : Commands
    }
}
