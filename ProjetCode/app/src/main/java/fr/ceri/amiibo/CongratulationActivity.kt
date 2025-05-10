package fr.ceri.amiibo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CongratulationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_congratulation)

        val congratsText = findViewById<TextView>(R.id.congratsText)
        val btnReturn = findViewById<Button>(R.id.btnReturn)

        congratsText.text = "🎉 Bravo ! Vous étes un Expert d'Amiibo 🎉"

        btnReturn.setOnClickListener {
            val intent = Intent(this, StartActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
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
