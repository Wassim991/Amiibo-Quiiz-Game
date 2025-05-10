package fr.ceri.amiibo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MusicBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_SCREEN_OFF -> {
                Log.d("MUSIC_RECEIVER", "Écran éteint, musique en pause")
                MusicManager.pauseBackgroundMusic()
            }
            Intent.ACTION_USER_PRESENT -> {
                Log.d("MUSIC_RECEIVER", "Utilisateur présent, musique reprend")
                MusicManager.resumeBackgroundMusic()
            }
        }
    }
}
