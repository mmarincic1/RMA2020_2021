package ba.etf.rma21.projekat

import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Pitanje
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {
    @GET("kviz/{id}/pitanja")
    suspend fun getPitanja(@Path("id") idKviza: Int): List<Pitanje>

    @GET("kviz")
    suspend fun getAll(): List<Kviz>
}