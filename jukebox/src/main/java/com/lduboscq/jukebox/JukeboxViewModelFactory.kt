package com.lduboscq.jukebox

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class JukeboxViewModelFactory(private val uri: Uri) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Uri::class.java)
            .newInstance(uri)
    }
}