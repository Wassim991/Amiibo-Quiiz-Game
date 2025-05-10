package fr.ceri.amiibo

import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import fr.ceri.amiibo.MusicBroadcastReceiver

class StartActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var musicReceiver: MusicBroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val logo = findViewById<ImageView>(R.id.logoNintendo)
        val startButton = findViewById<Button>(R.id.startButton)

        //Music
        MusicManager.startBackgroundMusic(this)

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_USER_PRESENT)
        }
        musicReceiver = MusicBroadcastReceiver()
        registerReceiver(musicReceiver, filter)

        // Animation de démarrage pour le logo
        val logoAnim = AnimationUtils.loadAnimation(this, R.anim.logo_enter)
        logo.startAnimation(logoAnim)

        // Animation au clic du bouton
        startButton.setOnClickListener {
            val clickAnim = AnimationUtils.loadAnimation(this, R.anim.button_click)
            startButton.startAnimation(clickAnim)

            // pouR Lancer la page principale après une courte pause pour laisser l'effet visible
            startButton.postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
            }, 150)
        }

    }

    /*override fun onDestroy() {
        super.onDestroy()
    }*/
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(musicReceiver)
        mediaPlayer?.release()
        mediaPlayer = null
    }
    override fun onPause() {
        super.onPause()
        MusicManager.pauseBackgroundMusic()
    }

    override fun onResume() {
        super.onResume()
        MusicManager.resumeBackgroundMusic()
    }



}
