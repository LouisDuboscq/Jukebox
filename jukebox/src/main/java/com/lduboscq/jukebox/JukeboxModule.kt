package com.lduboscq.jukebox

import android.net.Uri
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val jukeboxModule = module{
    viewModel {   JukeboxViewModel() }
}

