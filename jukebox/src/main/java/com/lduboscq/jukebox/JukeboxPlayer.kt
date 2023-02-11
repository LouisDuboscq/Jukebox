package com.lduboscq.jukebox

import android.media.MediaPlayer
import android.net.Uri

class JukeboxPlayer {

    /**
     * We keep track of datasourceSet and playerPrepared to have a better management of orientation change
     */
    data class MediaPlayerWithDataSet(
        val mediaPlayer: MediaPlayer,
        val datasourceSet: Boolean,
        val playerPrepared: Boolean,
    )

    private val players: MutableMap<Uri, MediaPlayerWithDataSet> = mutableMapOf()

    fun reset(uri: Uri) {
        players[uri]?.mediaPlayer?.reset()
    }

    fun setDataSource(uri: Uri) {
        if (players[uri] == null) {
            players[uri] = MediaPlayerWithDataSet(MediaPlayer(), datasourceSet = false, playerPrepared = false)
        }

        if (!players[uri]!!.datasourceSet) {
            players[uri]?.mediaPlayer?.setDataSource(uri.toString())
            players[uri] = players[uri]!!.copy(datasourceSet = true)
        }
    }

    fun prepare(uri: Uri, onPrepared: (MediaPlayer) -> Unit) {
        val player = players[uri]
        player!!.mediaPlayer.setOnPreparedListener(onPrepared)
        if (!players[uri]!!.playerPrepared) {
            player.mediaPlayer.prepareAsync()
            players[uri] = players[uri]!!.copy(playerPrepared = true)
        }
    }

    fun setOnCompletionListener(uri: Uri, callback: (MediaPlayer) -> Unit) {
        players[uri]?.mediaPlayer?.setOnCompletionListener {
            callback(it)
        }
    }

    fun start(uri: Uri) {
        players[uri]?.mediaPlayer?.start()
    }

    fun currentPosition(uri: Uri): Int {
        return players[uri]!!.mediaPlayer.currentPosition
    }

    fun pause(uri: Uri) {
        players[uri]?.mediaPlayer?.pause()
    }

    fun seekTo(i: Int, uri: Uri) {
        players[uri]?.mediaPlayer?.seekTo(i)
    }

    fun stopAndRelease(uri: Uri) {
        with(players[uri]!!.mediaPlayer) {
            stop()
            release()
        }
    }
}
