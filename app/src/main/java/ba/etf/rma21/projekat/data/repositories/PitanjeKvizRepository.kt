package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.models.Pitanje
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

// - getPitanja(nazivKviza: String, nazivPredmeta: String): List<Pitanje>

class PitanjeKvizRepository {

    companion object{
        var odabranaGodina: Int = -1
        var odabraniPredmet: Int = -1
        var odabranaGrupa: Int = -1
        var odgovor = -1
        var indexPitanja = ""


        // - vraća sva pitanja na kvizu sa zadanim id-em
        // example: OrderedMap { "id": 1, "naziv": "P1", "tekstPitanja": "Koji je prvi odgovor?", "opcije": List [ "A", "B", "C" ], "tacan": 0 }]
        suspend fun getPitanja(idKviza:Int):List<Pitanje> {
            return withContext(Dispatchers.IO){
                val rezultat = ApiAdapter.retrofit.getPitanja(idKviza)
                rezultat.stream().forEach { x ->
                    x.opcijeDb = x.opcije.joinToString(separator = ",")
                    x.kvizId = idKviza
                }
                return@withContext rezultat
            }
        }

        suspend fun getPitanjaDb(idKviza:Int):List<Pitanje> {
            return withContext(Dispatchers.IO){
                val db = AppDatabase.getInstance(AccountRepository.getContext())
                val rezultat = db.pitanjeDao().getPitanja(idKviza)
                rezultat.stream().forEach { x ->
                    x.opcije = x.opcijeDb.split(",")
                }
                return@withContext rezultat
            }
        }

        // ovo nije fkt s neta nego samo iz baze !
        suspend fun getRezultatSaNeta(idKviza: Int): Int{
            return withContext(Dispatchers.IO){
                val pocetiKvizovi = TakeKvizRepository.getPocetiKvizovi()
                var kvizId = -1
                if (pocetiKvizovi != null) {
                    for(kviz in pocetiKvizovi){
                        if(kviz.id == idKviza)
                            kvizId = kviz.KvizId
                    }
                }
                return@withContext getRezultatZaKviz(kvizId)
            }
        }

        suspend fun getRezultatSaKvizaZaOdgovor(idKviza: Int, idPitanje: Int, odgovorInt: Int): Int{
            return withContext(Dispatchers.IO){
                val db = AppDatabase.getInstance(AccountRepository.getContext())
                val pitanja = db.pitanjeDao().getPitanja(idKviza)
                val odgovori = db.odgovorDao().getOdgovori(idKviza)
                var rezultat = 0.0
                for(pitanje in pitanja){
                    for(odgovor in odgovori){
                        if(odgovor.pitanjeId == pitanje.id && odgovor.odgovoreno == pitanje.tacan){
                            rezultat += (1/pitanja.size.toDouble())*100
                        }
                    }
                    if(pitanje.id == idPitanje && pitanje.tacan == odgovorInt){
                        rezultat += (1/pitanja.size.toDouble())*100
                    }
                }
                return@withContext rezultat.roundToInt()
            }
        }

        suspend fun getRezultatZaKviz(idKviza: Int): Int{
            return withContext(Dispatchers.IO){
                val db = AppDatabase.getInstance(AccountRepository.getContext())
                val pitanja = db.pitanjeDao().getPitanja(idKviza)
                val odgovori = db.odgovorDao().getOdgovori(idKviza)
                var rezultat = 0.0
                for(pitanje in pitanja){
                    for(odgovor in odgovori){
                        if(odgovor.pitanjeId == pitanje.id && odgovor.odgovoreno == pitanje.tacan){
                            rezultat += (1/pitanja.size.toDouble())*100
                        }
                    }
                }
                return@withContext rezultat.roundToInt()
            }
        }

        suspend fun getZavrsenKviz(idKviza: KvizTaken): Boolean{
            return withContext(Dispatchers.IO){
                val db = AppDatabase.getInstance(AccountRepository.getContext())
                val odgovori = db.odgovorDao().postojiOdgovor(idKviza.id)
                if(odgovori.isEmpty())
                    return@withContext false
                val pitanja = db.pitanjeDao().getPitanja(odgovori[0].kvizId)
                return@withContext pitanja.size == odgovori.size
            }
        }

        suspend fun getRezultatSaServisa(idKviza: Int): Int{
            return withContext(Dispatchers.IO){
                val pitanja = getPitanja(idKviza)
                val odgovori = OdgovorRepository.getOdgovoriKvizApi(idKviza)
                var rezultat = 0.0
                for(pitanje in pitanja){
                    for(odgovor in odgovori){
                        if(odgovor.pitanjeId == pitanje.id && odgovor.odgovoreno == pitanje.tacan){
                            rezultat += (1/pitanja.size.toDouble())*100
                        }
                    }
                }
                return@withContext rezultat.roundToInt()
            }
        }

        suspend fun getZavrsenKvizApi(idKviza: KvizTaken): Boolean{
            return withContext(Dispatchers.IO){
                val pocetiKvizovi = TakeKvizRepository.getPocetiKvizovi()
                var kvizId = -1
                if (pocetiKvizovi != null) {
                    for(kviz in pocetiKvizovi){
                        if(kviz.id == idKviza.id)
                            kvizId = kviz.KvizId
                    }
                }
                val pitanja = ApiAdapter.retrofit.getPitanja(kvizId)
                val odgovori = OdgovorRepository.getOdgovoriKvizApi(kvizId)
                return@withContext pitanja.size == odgovori.size
            }
        }


    }

}