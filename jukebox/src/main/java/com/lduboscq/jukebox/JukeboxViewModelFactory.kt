package com.lduboscq.jukebox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class JukeboxViewModelFactory(private val playWhenReady: Boolean) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return JukeboxViewModel(playWhenReady) as T
    }
}