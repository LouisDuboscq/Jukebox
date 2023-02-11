package com.lduboscq.jukebox

import android.media.MediaPlayer
import android.net.Uri

class JukeboxPlayer {

    private val players: MutableMap<Uri, MediaPlayer> = mutableMapOf()

    fun reset(uri: Uri) {
        players[uri]?.reset()
    }

    fun setDataSource(uri: Uri) {
        if (players[uri] == null) {
            players[uri] = MediaPlayer()
        }
        players[uri]?.setDataSource(uri.toString())
    }

    fun prepare(uri: Uri, onPrepared: (MediaPlayer) -> Unit) {
        val player = players[uri]
        player?.setOnPreparedListener(onPrepared)
        player?.prepareAsync()
    }

    fun setOnCompletionListener(uri: Uri, callback: (MediaPlayer) -> Unit) {
        players[uri]?.setOnCompletionListener {
            callback(it)
        }
    }

    fun start(uri: Uri) {
        players[uri]?.start()
    }

    fun currentPosition(uri: Uri): Int {
        return players[uri]!!.currentPosition
    }

    fun pause(uri: Uri) {
        players[uri]?.pause()
    }

    fun seekTo(i: Int, uri: Uri) {
        players[uri]?.seekTo(i)
    }

    fun stopAndRelease(uri: Uri) {
        with(players[uri]!!) {
            stop()
            release()
        }
    }
}
