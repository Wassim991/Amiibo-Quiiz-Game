package fr.ceri.amiibo

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper

import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.GestureDetector
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import fr.ceri.amiibo.databinding.ActivityGameBinding
import java.util.Locale

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private var currentQuestion: AmiiboQuestion? = null
    private var score = 0
    private var selectedGameSeries: List<String> = emptyList()
    private var menuGame: Menu? = null
    private lateinit var correctSound: MediaPlayer
    private lateinit var wrongSound: MediaPlayer
    private var scoreTextViewInMenu: TextView? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Recup depuis MainActivity
        selectedGameSeries = intent.getStringArrayListExtra("selectedGameSeries") ?: emptyList()
        loadQuestion()

        //recuperer les reponses selectionnée
        val answers = listOf(binding.btnProp1, binding.btnProp2, binding.btnProp3)
        answers.forEach { view ->
            view.setOnClickListener {
                checkAnswer(view.text.toString(), view)
            }
        }


        //pour le swipe
        val gestureDetector = GestureDetector(this, OnSwipeTouchListener.GestureListener(this, this))
        binding.root.setOnTouchListener(OnSwipeTouchListener(this, binding.root, gestureDetector))

        //pour les effets vrais faux
        correctSound = MediaPlayer.create(this, R.raw.correct)
        wrongSound = MediaPlayer.create(this, R.raw.wronganswer)


    }


    //Afficher Menu Avec icons
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_game, menu)
        menuGame = menu
        val scoreItem = menu?.findItem(R.id.item_game_score)
        val actionView = scoreItem?.actionView
        scoreTextViewInMenu = actionView?.findViewById(R.id.scoreMenuText)

        updateScore()
        return true
    }
    //recuperer la question avec les propos
    private fun loadQuestion(type: String = "random") {
        val question = AmiiboApplication.genererQuestion(type, selectedGameSeries)
        updateUIWithQuestion(question)
    }

    private fun checkAnswer(selected: String, button: TextView) {
        val correct = currentQuestion?.bonneReponse
        if (selected == correct) {
            button.setBackgroundColor(Color.GREEN)  // Bonne réponse = Vert
            correctSound.start()
            score += 2
            Toast.makeText(this, "✅ Bonne réponse !", Toast.LENGTH_SHORT).show()
        } else {
            button.setBackgroundColor(Color.RED)   // Mauvaise réponse = Rouge
            wrongSound.start()
            score -= 2
            Toast.makeText(this, "❌ Mauvaise réponse", Toast.LENGTH_SHORT).show()
        }

        if (score >= 12) {
            val intent = Intent(this, CongratulationActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        updateScore()

        // Delay pour voir la couleur puis nouvelle question
        Handler(Looper.getMainLooper()).postDelayed({
            loadQuestion() // Recharge une nouvelle question
        }, 1000)
    }


    fun handleSwipe(type: String) {
        score -= 1
        currentQuestion = AmiiboApplication.genererQuestion(type, selectedGameSeries)
        updateUIWithQuestion(currentQuestion)
        updateScore()
    }

    //Juste Pour handle si il yas un erreur et  pour configurer le UI
    private fun updateUIWithQuestion(question: AmiiboQuestion?) {
        if (question == null) {
            Toast.makeText(this, "Erroorr", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        currentQuestion = question

        binding.questionText.text = when (question.typeQuestion) {
            "gameSeries" -> "De quel GameSerie vient cet Amiibo ?"
            "name" -> "Quel est le nom de cet Amiibo ?"
            else -> ""
        }

        Glide.with(this).load(question.imageUrl).into(binding.amiiboImage)

        val props = question.propositions
        binding.btnProp1.text = props[0]
        binding.btnProp2.text = props[1]
        binding.btnProp3.text = props[2]
        // Remettre les couleurs par défaut
        val defaultColor = ContextCompat.getColor(this, R.color.blue)

        binding.btnProp1.setBackgroundColor(defaultColor)
        binding.btnProp2.setBackgroundColor(defaultColor)
        binding.btnProp3.setBackgroundColor(defaultColor)

    }

    private fun updateScore() {
        val color = when {
            score > 0 -> Color.GREEN
            score == 0 -> Color.BLACK
            else -> Color.RED
        }

        scoreTextViewInMenu?.apply {
            text = "Score: $score"
            setTextColor(color)
        }

        //  pour le TextView dans le layout,
        //binding.scoreTextView.text = "Score : $score"
        //binding.scoreTextView.setTextColor(color)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.item_game_reset -> {
                score = 0
                updateScore()
                Toast.makeText(this, "Score réinitialisé !", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.item_game_home -> {
                val intent = Intent(this, StartActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    //Pour liberer Les ressources
    override fun onDestroy() {
        super.onDestroy()
        correctSound.release()
        wrongSound.release()
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
fun String.parseAsHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}

fun MenuItem.setTitleColor(color: Int) {
    val hexColor = Integer.toHexString(color).uppercase().padStart(6, '0')
    val html = "<font color='#$hexColor'>$title</font>"
    this.title = html.parseAsHtml()
}



