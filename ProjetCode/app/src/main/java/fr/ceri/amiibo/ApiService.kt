package fr.ceri.amiibo

import fr.ceri.amiibo.AmiiboHeader

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/gameseries")
    fun getGameSeries(): Call<AmiiboGameHeader>

    @GET("api/amiibo/")
    fun getAmiibosByGameSeries(@Query("gameseries") key: String): Call<AmiiboHeader>
}
