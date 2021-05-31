package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.models.OdgPitBod
import ba.etf.rma21.projekat.data.models.Odgovor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class OdgovorRepository {
    companion object {
        //- vraća listu odgovora za kviz, praznu listu ako student nije odgovarao ili nije upisan na zadani kviz
        suspend fun getOdgovoriKviz(idKviza: Int): List<Odgovor> {
            return withContext(Dispatchers.IO) {
                val acc = AccountRepository()
                val pokrenutiKvizovi = ApiAdapter.retrofit.getPocetiKvizovi(acc.getHash())
                var pKvizi : Int = -1
                for(pKviz in pokrenutiKvizovi){
                    if(pKviz.KvizId == idKviza){
                        pKvizi = pKviz.id
                        break
                    }
                }
                if(pKvizi == -1)
                    return@withContext emptyList<Odgovor>()
                try {
                    val rezultat = ApiAdapter.retrofit.getOdgovoriKviz(pKvizi, acc.getHash())
                    return@withContext rezultat
                }
                catch (e: Exception){
                    return@withContext emptyList<Odgovor>()
                }
            }
        }

        // - funkcija postavlja odgovor sa indeksom odgovor na pitanje sa id-em idPitanje u okviru pokušaja sa id-em
        //    idKvizTaken. Funkcija vraća ukupne bodove na kvizu nakon odgovora ili -1 ukoliko ima neka greška u zahtjevu
        suspend fun postaviOdgovorKviz(idKvizTaken:Int, idPitanje:Int, odgovor:Int):Int{
            return withContext(Dispatchers.IO){
                val bodovi = PitanjeKvizRepository.getRezultatSaKvizaZaOdgovor(idKvizTaken, idPitanje, odgovor)
                val odgovor = OdgPitBod(odgovor = odgovor, pitanje = idPitanje, bodovi = bodovi)
                val acc = AccountRepository()
                try {
                    ApiAdapter.retrofit.postaviOdgovorKviz(acc.getHash(), idKvizTaken, odgovor)
                    return@withContext bodovi
                }catch(e: Exception){
                    return@withContext -1
                }
            }
        }

        suspend fun getOdgovorKviz(idKvizTaken:Int, idPitanje:Int): Int{
            return withContext(Dispatchers.IO) {
                val acc = AccountRepository()
                var listaOdgovora = ApiAdapter.retrofit.getOdgovoriKviz(idKvizTaken, acc.getHash())
                for(odgovor in listaOdgovora){
                    if(odgovor.pitanjeId == idPitanje)
                        return@withContext odgovor.odgovoreno
                }
                return@withContext -1
            }
        }
    }
}