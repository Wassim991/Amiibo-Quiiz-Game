package fr.ceri.amiibo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.ceri.amiibo.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var amiiboGames: List<AmiiboGame>
    private var selectAllMenuItem: MenuItem? = null
    private lateinit var gameSeriesList: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ApiClient.apiService.getGameSeries().enqueue(object : Callback<AmiiboGameHeader> {
            override fun onResponse(
                call: Call<AmiiboGameHeader>,
                response: Response<AmiiboGameHeader>
            ) {
                if (response.isSuccessful) {
                    //On Recupére Tout Les Amiibo Depuis lA Repponse
                    amiiboGames = response.body()?.amiibo ?: emptyList()

                    //Pour Filtrer la liste
                    gameSeriesList = amiiboGames.map { it.name }.distinct()

                    //Les Injecters dans la liste de layout j'ai utilisée ArrayAdapter Pour faciliter l'injection directe dans la listView
                    val adapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_list_item_multiple_choice,
                        gameSeriesList
                    )

                    binding.listViewGameSeries.adapter = adapter
                    binding.listViewGameSeries.choiceMode = ListView.CHOICE_MODE_MULTIPLE

                    // Sélectionner 4 au hasard
                    if (gameSeriesList.size >= 4) {
                        val randomIndices = (0 until gameSeriesList.size).shuffled().take(4)
                        for (index in randomIndices) {
                            binding.listViewGameSeries.setItemChecked(index, true)
                        }
                    }
                }
            }


            override fun onFailure(call: Call<AmiiboGameHeader>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Erreur API : ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    //Pour Afficher Les items dans le menu d'option
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        selectAllMenuItem = menu?.findItem(R.id.item_select_all)
        menuInflater.inflate(R.menu.menu_getamiibos, menu)
        return true
    }

    //uniquement appeler quand l'utilisateur clique sur un item de menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            //Cas de Selectionneer Tout
            R.id.item_select_all -> {
                val count = binding.listViewGameSeries.adapter.count
                val isAllSelected = (0 until count).all { binding.listViewGameSeries.isItemChecked(it) } //check si tout cochéee

                //forces en cas ou il ont pas tout selectionnée
                for (i in 0 until count) {
                    binding.listViewGameSeries.setItemChecked(i, !isAllSelected)
                }

                item.setIcon(
                    if (!isAllSelected) R.drawable.ic_deselect_all
                    else R.drawable.ic_select_all
                )

                true
            }

            //Cas ou on appuis sur le joysitck
            R.id.item_validate -> {
                val selectedNames = mutableListOf<String>()
                for (i in 0 until gameSeriesList.size) {
                    if (binding.listViewGameSeries.isItemChecked(i)) {
                        selectedNames.add(gameSeriesList[i])
                    }
                }

                if (selectedNames.size < 4) {
                    Toast.makeText(
                        this,
                        "Veuillez sélectionner au moins 4 gameseries",
                        Toast.LENGTH_SHORT
                    ).show()
                    return true
                }
                //Pour recuperer Les noms
                val selectedGames = amiiboGames.filter { it.name in selectedNames }

                var requestsFinished = 0

                for (game in selectedGames) {
                    ApiClient.apiService.getAmiibosByGameSeries(game.key)
                        .enqueue(object : Callback<AmiiboHeader> {
                            override fun onResponse(

                                call: Call<AmiiboHeader>,
                                response: Response<AmiiboHeader>
                            ) {
                                if (response.isSuccessful) {
                                    val amiibos = response.body()?.amiibo ?: emptyList()

                                    AmiiboApplication.insererAmiibosDansRealm(amiibos)
                                }

                                requestsFinished++
                                if (requestsFinished == selectedGames.size) {
                                    val intent = Intent(this@MainActivity, GameActivity::class.java)
                                    intent.putStringArrayListExtra(
                                        "selectedGameSeries",
                                        ArrayList(selectedNames)
                                    )
                                    startActivity(intent)
                                }
                            }

                            override fun onFailure(call: Call<AmiiboHeader>, t: Throwable) {
                                requestsFinished++
                                Toast.makeText(
                                    this@MainActivity,
                                    "Erreur API : ${t.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                }

                true
            }

            else -> super.onOptionsItemSelected(item)
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
