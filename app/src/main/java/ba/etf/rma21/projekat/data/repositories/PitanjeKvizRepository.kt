package ba.etf.rma21.projekat.data.repositories

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
                return@withContext ApiAdapter.retrofit.getPitanja(idKviza)
            }
        }

        suspend fun getRezultatSaNeta(idKviza: Int): Int{
            return withContext(Dispatchers.IO){
                val pocetiKvizovi = TakeKvizRepository.getPocetiKvizovi()
                var kvizId = -1
                for(kviz in pocetiKvizovi){
                    if(kviz.id == idKviza)
                        kvizId = kviz.KvizId
                }
                val pitanja = getPitanja(kvizId)
                val odgovori = OdgovorRepository.getOdgovoriKviz(idKviza)
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

        suspend fun getRezultatSaKvizaZaOdgovor(idKviza: Int, idPitanje: Int, odgovorInt: Int): Int{
            return withContext(Dispatchers.IO){
                val pocetiKvizovi = TakeKvizRepository.getPocetiKvizovi()
                var kvizId = -1
                for(kviz in pocetiKvizovi){
                    if(kviz.id == idKviza)
                        kvizId = kviz.KvizId
                }
                val pitanja = getPitanja(kvizId)
                val odgovori = OdgovorRepository.getOdgovoriKviz(idKviza)
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

        suspend fun getZavrsenKviz(idKviza: KvizTaken): Boolean{
            return withContext(Dispatchers.IO){
                val pocetiKvizovi = TakeKvizRepository.getPocetiKvizovi()
                var kvizId = -1
                for(kviz in pocetiKvizovi){
                    if(kviz.id == idKviza.id)
                        kvizId = kviz.KvizId
                }
                val pitanja = ApiAdapter.retrofit.getPitanja(kvizId)
                val odgovori = OdgovorRepository.getOdgovoriKviz(idKviza.id)
                return@withContext pitanja.size == odgovori.size
            }
        }

        suspend fun getRezultatSaNetaZaKviz(kviz: Kviz): Int{
            return withContext(Dispatchers.IO){
                val acc = AccountRepository()
                val pokrenutiKvizovi = ApiAdapter.retrofit.getPocetiKvizovi(acc.getHash())
                var imaGa = false
                lateinit var pKvizi: KvizTaken
                for(pKviz in pokrenutiKvizovi){
                    if(pKviz.KvizId == kviz.id){
                        pKvizi = pKviz
                        imaGa = true
                        break
                    }
                }
                var rezultat = -1.0
                if(imaGa) {
                    val pitanja = getPitanja(kviz.id)
                    val odgovori = OdgovorRepository.getOdgovoriKviz(pKvizi.id)
                    rezultat = 0.0
                    for (pitanje in pitanja) {
                        for (odgovor in odgovori) {
                            if (odgovor.pitanjeId == pitanje.id && odgovor.odgovoreno == pitanje.tacan) {
                                rezultat += (1 / pitanja.size.toDouble()) * 100
                            }
                        }
                    }
                }
                return@withContext rezultat.roundToInt()
            }
        }

    }

}