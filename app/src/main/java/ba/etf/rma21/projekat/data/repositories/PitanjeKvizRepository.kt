package ba.etf.rma21.projekat.data.repositories

import android.text.BoringLayout
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.pitanjaKvizovi
import java.util.stream.Collectors
import ba.etf.rma21.projekat.data.getPitanja
import ba.etf.rma21.projekat.data.models.PitanjeKviz

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

         fun getPitanja(nazivKviza: String, nazivPredmeta: String): List<Pitanje>{
             var rezultatnaPitanja = mutableListOf<Pitanje>()
                 var listaPitanja =
                     pitanjaKvizovi().stream().filter { x -> x.kviz == nazivKviza }.collect(
                         Collectors.toList()
                     )
                 var pomocnaPitanja = getPitanja()
                 for (pitanje in pomocnaPitanja) {
                     for (kviz in listaPitanja) {
                         if (pitanje.naziv == kviz.naziv){
                             rezultatnaPitanja.add(pitanje)
                             var pom = PitanjeKviz(pitanje.naziv, nazivKviza)
                             pom.setNazivPredmeta(nazivPredmeta)
                             if(!listaSvihOdgovorenihPitanja.contains(pom))
                                listaSvihOdgovorenihPitanja.add(pom)
                         }
                     }
                 }
            return rezultatnaPitanja
        }

        fun odgovoriNaPitanje(odgovor: Int, nazivKviza: String, nazivPredmeta: String, nazivPitanja: String){
            var pom = PitanjeKviz(nazivPitanja, nazivKviza)
            pom.setNazivPredmeta(nazivPredmeta)
            for(pom1 in listaSvihOdgovorenihPitanja){
                if(pom1.equals(pom)){
                    pom1.setOdgovorNaPitanje(odgovor)
                    break
                }
            }
        }

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

        fun zavrsiKviz(nazivKviza: String, nazivPredmeta: String){
            listaSvihOdgovorenihPitanja.stream().filter{x ->
                x.getNazivPredmeta() == nazivPredmeta && x.kviz == nazivKviza
            }.forEach { x -> x.setZavrsenKviz() }
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

        fun getZavrsenKviz(nazivKviza: String, nazivPredmeta: String): Boolean{
            val pom: PitanjeKviz = listaSvihOdgovorenihPitanja.stream().filter {
                    pitanje ->
                pitanje.kviz == nazivKviza &&
                        pitanje.getNazivPredmeta() == nazivPredmeta
            }.findFirst().get()
            return pom.getZavrsenKviz()
        }

        fun dodajRezultat(uradjeniKviz: String, uradjeniPredmet: String, rezultat: Double) {
            val pom: PitanjeKviz = listaSvihOdgovorenihPitanja.stream().filter {
                    pitanje ->
                pitanje.kviz == uradjeniKviz &&
                        pitanje.getNazivPredmeta() == uradjeniPredmet
            }.findFirst().get()
           pom.dodajRezultat(rezultat)
        }

    }

}