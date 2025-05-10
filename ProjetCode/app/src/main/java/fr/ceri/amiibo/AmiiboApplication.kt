package fr.ceri.amiibo

import android.util.Log
import android.app.Application
import fr.ceri.amiibo.AmiiboQuestion
import fr.ceri.amiibo.*
import io.realm.Realm
import io.realm.RealmConfiguration


class AmiiboApplication : Application() {

    companion object {
        lateinit var realm: Realm


        fun genererQuestion(type: String = "random", gameSeriesOptions: List<String>): AmiiboQuestion? {

            //On Recupére tout les Amiibo  Realm
            val allAmiibos = realm.where(Amiibo::class.java).findAll()
                .filter { it.name != null && it.image != null && it.gameSeries != null }

            if (allAmiibos.size < 3 || gameSeriesOptions.isEmpty()) return null

            //Determiner Type De Question de facon random soit Name ou soit GS
            val questionType = when (type) {
                "name", "gameSeries" -> type
                else -> if ((0..1).random() == 0) "name" else "gameSeries"
            }

            //Filtrer Les Amiibos de façon ce que l'utilisateur a selecionnée
            val filteredAmiibos = when (questionType) {
                "gameSeries" -> allAmiibos.filter { it.gameSeries in gameSeriesOptions }
                "name" -> allAmiibos.filter { it.gameSeries in gameSeriesOptions }
                else -> allAmiibos
            }


            if (filteredAmiibos.size < 3) return null

            val correctAmiibo = filteredAmiibos.random()
            val imageUrl = correctAmiibo.image
            val correctAnswer = when (questionType) {
                "name" -> correctAmiibo.name
                "gameSeries" -> correctAmiibo.gameSeries
                else -> ""
            }

            //Pour Prendre deux Proposition Incorrecte
            val options = when (questionType) {
                "name" -> filteredAmiibos.map { it.name }.filter { it != correctAnswer }.distinct()
                "gameSeries" -> gameSeriesOptions.filter { it != correctAnswer }.distinct()
                else -> emptyList()
            }.shuffled().take(2)

            val propositions = (options + correctAnswer).shuffled()

            Log.d("QUIZ_DEBUG", "Selected GameSeries (User) = $gameSeriesOptions")
            Log.d("QUIZ_DEBUG", "QuestionType = $questionType")
            Log.d("QUIZ_DEBUG", "Correct Amiibo = ${correctAmiibo.name} (${correctAmiibo.gameSeries})")
            Log.d("QUIZ_DEBUG", "CorrectAnswer = $correctAnswer")
            Log.d("QUIZ_DEBUG", "Propositions = $propositions")

            if (questionType == "gameSeries" && correctAnswer !in gameSeriesOptions) {
                Log.e("QUIZ_DEBUG", "❌ ERREUR : La bonne GameSeries n'est pas dans la sélection utilisateur !")
                return null
            }

            return AmiiboQuestion(
                imageUrl = imageUrl,
                propositions = propositions,
                bonneReponse = correctAnswer,
                typeQuestion = questionType
            )
        }


        fun insererAmiibosDansRealm(amiibos: List<AmiiboFull>) {
            realm.executeTransactionAsync { realmTransaction ->
                for (a in amiibos) {
                    val exists = realmTransaction.where(Amiibo::class.java)
                        .equalTo("id", a.image)
                        .findFirst() != null

                    if (!exists) {
                        val newAmiibo = Amiibo().apply {
                            id = a.image
                            name = a.name
                            gameSeries = a.gameSeries
                            image = a.image
                        }
                        realmTransaction.insert(newAmiibo)
                    }
                }
            }
        }







    }

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("amiibo.realm")
            .deleteRealmIfMigrationNeeded()
            .build()

        realm = Realm.getInstance(config)
    }

}
