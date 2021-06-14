package ba.etf.rma21.projekat.data.repositories

import android.content.Context
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.AppDatabase_Impl
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.models.OdgPitBod
import ba.etf.rma21.projekat.data.models.Odgovor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class OdgovorRepository {
    companion object {
        private lateinit var context: Context
        fun getContext(): Context {
            return context
        }


        fun setContext(_context: Context) {
            context = _context
        }

        //- vraća listu odgovora za kviz, praznu listu ako student nije odgovarao ili nije upisan na zadani kviz
        suspend fun getOdgovoriKviz(idKviza: Int): List<Odgovor> {
            return withContext(Dispatchers.IO) {
                val db = AppDatabase.getInstance(AccountRepository.getContext())
                val odgovori = db.odgovorDao().getOdgovori(idKviza)
                if(odgovori.isEmpty())
                    return@withContext emptyList<Odgovor>()
                else return@withContext odgovori
            }
        }

        suspend fun getOdgovoriKvizApi(idKviza: Int): List<Odgovor> {
            return withContext(Dispatchers.IO) {
                val pokrenutiKvizovi = ApiAdapter.retrofit.getPocetiKvizovi(AccountRepository.getHash())
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
                    val rezultat = ApiAdapter.retrofit.getOdgovoriKviz(pKvizi, AccountRepository.getHash())
                    return@withContext rezultat
                }
                catch (e: Exception){
                    return@withContext emptyList<Odgovor>()
                }
            }
        }

        // - funkcija postavlja odgovor sa indeksom odgovor na pitanje sa id-em idPitanje u okviru pokušaja sa id-em
        //    idKvizTaken. Funkcija vraća ukupne bodove na kvizu nakon odgovora ili -1 ukoliko ima neka greška u zahtjevu


        suspend fun postaviOdgovorKvizApi(idKvizTaken:Int, idPitanje:Int, odgovor:Int):Int{
            return withContext(Dispatchers.IO){
                val bodovi = PitanjeKvizRepository.getRezultatZaKviz(KvizRepository.pokrenutiKviz.id)
                //val bodovi = PitanjeKvizRepository.getRezultatSaKvizaZaOdgovor(KvizRepository.pokrenutiKviz.id, idPitanje, odgovor)
                val odgovor1 = OdgPitBod(odgovor = odgovor, pitanje = idPitanje, bodovi = bodovi)
                try {
                    ApiAdapter.retrofit.postaviOdgovorKviz(AccountRepository.getHash(), idKvizTaken, odgovor1)
                    return@withContext bodovi
                }catch(e: Exception){
                    return@withContext -1
                }
            }
        }

        // SADA MORA NA BAZU DA POSTAVI ODGOVOR !!!
        suspend fun postaviOdgovorKviz(idKvizTaken:Int, idPitanje:Int, odgovor:Int): Int{
            return withContext(Dispatchers.IO) {
                val db = AppDatabase.getInstance(AccountRepository.getContext())
                val odgovori = db.odgovorDao().postojiOdgovor(idKvizTaken)
                var vecOdgovoreno = false
                for (odgovor in odgovori) {
                    if (odgovor.pitanjeId == idPitanje) {
                        vecOdgovoreno = true
                        break
                    }
                }
                val pocetiKvizovi = TakeKvizRepository.getPocetiKvizovi()
                var kvizId = -1
                if (pocetiKvizovi != null) {
                    for (kviz in pocetiKvizovi) {
                        if (kviz.id == idKvizTaken)
                            kvizId = kviz.KvizId
                    }
                }
                if (!vecOdgovoreno) {
                    val bodovi = PitanjeKvizRepository.getRezultatSaKvizaZaOdgovor(kvizId, idPitanje, odgovor)
                    try {
                        val odgovor = Odgovor(db.odgovorDao().getAllOdgovori().size, odgovor, idPitanje, kvizId, idKvizTaken)
                        db.odgovorDao().dodajOdgovor(odgovor)
                        return@withContext bodovi
                    } catch (e: Exception) {
                        return@withContext -1
                    }
                }
                return@withContext PitanjeKvizRepository.getRezultatZaKviz(kvizId)
            }
        }

        // SADA DOHVACA ODGOVOR SA BAZE !!!!
        suspend fun getOdgovorKviz(idKvizTaken:Int, idPitanje:Int): Int{
            return withContext(Dispatchers.IO) {
                //var listaOdgovora = ApiAdapter.retrofit.getOdgovoriKviz(idKvizTaken, AccountRepository.getHash())
                val db = AppDatabase.getInstance(AccountRepository.getContext())
                val listaOdgovora = db.odgovorDao().postojiOdgovor(idKvizTaken)
                for(odgovor in listaOdgovora){
                    if(odgovor.pitanjeId == idPitanje)
                        return@withContext odgovor.odgovoreno
                }
                return@withContext -1
            }
        }

        // ovo jer je bilo u specifikaciji spirale
        suspend fun predajOdgovore(kvizId: Int){
            return withContext(Dispatchers.IO){
                val db = AppDatabase.getInstance(AccountRepository.getContext())
                val odgovori = db.odgovorDao().getOdgovori(kvizId)
                for(odgovor in odgovori){
                    postaviOdgovorKvizApi(odgovor.idKvizTaken, odgovor.pitanjeId, odgovor.odgovoreno)
                }
                // jos postavidi bodove i predan odmah
                db.kvizDao().zavrsiKviz(true, KvizRepository.pokrenutiKviz.id)
                db.kvizDao().upisiBodove(PitanjeKvizRepository.getRezultatZaKviz(KvizRepository.pokrenutiKviz.id), KvizRepository.pokrenutiKviz.id)
            }
        }
    }
}