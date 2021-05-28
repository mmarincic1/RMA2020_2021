package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.models.PitanjeKviz
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

// - getPitanja(nazivKviza: String, nazivPredmeta: String): List<Pitanje>

class PitanjeKvizRepository {

    companion object{

        var listaSvihOdgovorenihPitanja = mutableListOf<PitanjeKviz>()
        var odabranaGodina: Int = -1
        var odabraniPredmet: Int = -1
        var odabranaGrupa: Int = -1
        var uradjeniKviz = ""
        var uradjeniPredmet = ""
        var brojPitanja = 0
        var odgovor = -1
        var indexPitanja = ""


        fun getOdgovorNaPitanje(nazivKviza: String, nazivPredmeta: String, nazivPitanja: String): Int{
            if(listaSvihOdgovorenihPitanja.stream().filter {
                pitanje ->
                pitanje.naziv == nazivPitanja &&
                        pitanje.kviz == nazivKviza &&
                        pitanje.getNazivPredmeta() == nazivPredmeta
            }.findFirst().isPresent) {
                val pom: PitanjeKviz = listaSvihOdgovorenihPitanja.stream().filter { pitanje ->
                    pitanje.naziv == nazivPitanja &&
                            pitanje.kviz == nazivKviza &&
                            pitanje.getNazivPredmeta() == nazivPredmeta
                }.findFirst().get()
                return pom.getOdgovorNaPitanje()
            }
            return -1
        }


        fun getRezultat(nazivKviza: String, nazivPredmeta: String): Int{
            if(listaSvihOdgovorenihPitanja.stream().filter {
                        pitanje ->
                    pitanje.kviz == nazivKviza &&
                            pitanje.getNazivPredmeta() == nazivPredmeta
                }.findFirst().isPresent){
            val pom: PitanjeKviz = listaSvihOdgovorenihPitanja.stream().filter {
                    pitanje ->
                        pitanje.kviz == nazivKviza &&
                        pitanje.getNazivPredmeta() == nazivPredmeta
            }.findFirst().get()
            return pom.getRezultat()
            }
            return -1
        }



        // - vraća sva pitanja na kvizu sa zadanim id-em
        // example: OrderedMap { "id": 1, "naziv": "P1", "tekstPitanja": "Koji je prvi odgovor?", "opcije": List [ "A", "B", "C" ], "tacan": 0 }]
        suspend fun getPitanja(idKviza:Int):List<Pitanje> {
            return withContext(Dispatchers.IO){
                return@withContext ApiAdapter.retrofit.getPitanja(idKviza)
            }
        }

        suspend fun getRezultatSaNeta(idKviza: Int): Int{
            return withContext(Dispatchers.IO){
                var pom = idKviza
                val pitanja = getPitanja(KvizRepository.pokrenutiKviz.id)
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

        suspend fun getZavrsenKviz(idKviza: KvizTaken): Boolean{
            return withContext(Dispatchers.IO){
                var pomocni = idKviza
                val pitanja = ApiAdapter.retrofit.getPitanja(KvizRepository.pokrenutiKviz.id)
                val odgovori = OdgovorRepository.getOdgovoriKviz(idKviza.id)
                return@withContext pitanja.size == odgovori.size
            }
        }

    }

}