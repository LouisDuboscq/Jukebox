package com.lduboscq.jukebox

import java.util.concurrent.TimeUnit

fun Long.toTimeText(): String {
    val position = this
    val time = String.format(
        "%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(position),
        TimeUnit.MILLISECONDS.toSeconds(position) - TimeUnit.MINUTES.toSeconds(
            TimeUnit.MILLISECONDS.toMinutes(position)
        )
    )
    return time
}
