package fr.ceri.amiibo

import android.content.Context
import android.media.MediaPlayer

object MusicManager {
    private var mediaPlayer: MediaPlayer? = null
    private var isPausedManually = false

    fun startBackgroundMusic(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context.applicationContext, R.raw.coral_chorus)
            mediaPlayer?.isLooping = true
        }

        if (!mediaPlayer!!.isPlaying && !isPausedManually) {
            mediaPlayer?.start()
        }
    }

    fun pauseBackgroundMusic() {
        if (isPlaying()) {
            mediaPlayer?.pause()
            isPausedManually = true
        }
    }

    fun resumeBackgroundMusic() {
        if (mediaPlayer != null && isPausedManually) {
            mediaPlayer?.start()
            isPausedManually = false
        }
    }

    fun stopBackgroundMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        isPausedManually = false
    }

    private fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }
}
