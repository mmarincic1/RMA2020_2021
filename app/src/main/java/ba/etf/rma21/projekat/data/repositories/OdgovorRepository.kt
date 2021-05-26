package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Odgovor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OdgovorRepository {
    companion object {
        //- vraća listu odgovora za kviz, praznu listu ako student nije odgovarao ili nije upisan na zadani kviz
        suspend fun getOdgovoriKviz(idKviza: Int): List<Odgovor> {
            return withContext(Dispatchers.IO) {
                val acc = AccountRepository()
                return@withContext ApiAdapter.retrofit.getOdgovoriKviz(idKviza, acc.getHash())
            }
        }

        // - funkcija postavlja odgovor sa indeksom odgovor na pitanje sa id-em idPitanje u okviru pokušaja sa id-em
        //    idKvizTaken. Funkcija vraća ukupne bodove na kvizu nakon odgovora ili -1 ukoliko ima neka greška u zahtjevu
//    suspend fun postaviOdgovorKviz(idKvizTaken:Int, idPitanje:Int, odgovor:Int):Int{
//        return withContext(Dispatchers.IO){
//            return@withContext ApiAdapter.retrofit.postaviOdgovorKviz(idKvizTaken, idPitanje, odgovor)
//        }
//    }
    }
}