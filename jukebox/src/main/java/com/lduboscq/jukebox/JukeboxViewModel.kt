package com.lduboscq.jukebox

import android.media.MediaPlayer
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

class JukeboxViewModel : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state = _state.asStateFlow()

    private val mediaPlayer = MediaPlayer()

    private var refreshJob: Job? = null

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    sealed interface Effect {
        object AudioListened : Effect
        object FileNotFound : Effect
    }

    sealed interface State {
        object Loading : State
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

    fun initUri(uri: Uri) {
        Log.d("JukeboxViewModel", "uri : $uri")
        mediaPlayer.reset()
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                mediaPlayer.setDataSource(uri.toString())
            } catch (e: FileNotFoundException) {
                _effect.send(Effect.FileNotFound)
                _state.value = State.FileNotFound
                return@launch
            } catch (e: IOException) {
                _effect.send(Effect.FileNotFound)
                _state.value = State.FileNotFound
                return@launch
            }

            mediaPlayer.setOnPreparedListener { player ->
                _state.value = State.Loaded(mediaPlayerDuration = mediaPlayer.duration)
            }
            mediaPlayer.setOnCompletionListener {
                _state.value = State.Loaded(
                    mediaPlayerDuration = mediaPlayer.duration,
                    mediaPlayerPosition = 0,
                    playingState = PlayingState.Pause
                )
                resetToStart()
                viewModelScope.launch { _effect.send(Effect.AudioListened) }
            }
            mediaPlayer.prepareAsync()
        }
    }

    /**
     * @throws IllegalStateException if play is not called in Loaded state
     */
    fun play() {
        require(_state.value is State.Loaded)
        refreshJob = viewModelScope.launch {
            while (true) {
                _state.value = (_state.value as State.Loaded).copy(
                    mediaPlayerPosition = mediaPlayer.currentPosition,
                )
                delay(1000)
            }
        }
        _state.value = (_state.value as State.Loaded).copy(playingState = PlayingState.Playing)
        mediaPlayer.start()
    }

    /**
     * @throws IllegalStateException if pause is not called in Loaded state
     */
    fun pause() {
        check(_state.value is State.Loaded)
        refreshJob?.cancel()
        try {
            mediaPlayer.pause()
        } catch (e: IllegalStateException) {
            Log.e("ListenAudioViewModel", "pause $e")
        }
        _state.value = (_state.value as State.Loaded).copy(playingState = PlayingState.Pause)
    }

    private fun resetToStart() {
        check(_state.value is State.Loaded)
        refreshJob?.cancel()
        mediaPlayer.seekTo(0)
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
        with(mediaPlayer) {
            stop()
            release()
        }
    }

    fun onSeek(sliderPosition: Float) {
        if (_state.value is State.Loaded) {
            mediaPlayer.seekTo(sliderPosition.toInt())
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
