package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.pitanjaKvizovi
import java.util.stream.Collectors
import ba.etf.rma21.projekat.data.getPitanja
import ba.etf.rma21.projekat.data.models.PitanjeKviz

// - getPitanja(nazivKviza: String, nazivPredmeta: String): List<Pitanje>

class PitanjeKvizRepository {

    companion object{
        // nisam iskorisio nazivPredmeta jer ne bi ovo pozivao da nije kliknuo na vec neki odabrani kviz
        // koji se nalazi u kvizRepository-u
        var listaSvihOdgovorenihPitanja = mutableListOf<PitanjeKviz>()

         fun getPitanja(nazivKviza: String, nazivPredmeta: String): List<Pitanje>{
             var rezultatnaPitanja = mutableListOf<Pitanje>()
                 // prvi put ulazi u pitanja
                 var listaPitanja =
                     pitanjaKvizovi().stream().filter { x -> x.kviz == nazivKviza }.collect(
                         Collectors.toList()
                     )
                 var pomocnaPitanja = getPitanja()
                 for (pitanje in pomocnaPitanja) {
                     for (kviz in listaPitanja) {
                         if (pitanje.naziv == kviz.naziv){
                             rezultatnaPitanja.add(pitanje)
//                             var pom = PitanjeKviz(pitanje.naziv, nazivKviza)
//                             pom.setNazivPredmeta(nazivPredmeta)
//                             listaSvihOdgovorenihPitanja.add(pom)
                         }
                     }
                 }
            return rezultatnaPitanja
        }

//        fun odgovoriNaPitanje(odgovor: Int, nazivKviza: String, nazivPredmeta: String, nazivPitanja: String){
//            var pom = PitanjeKviz(nazivPitanja, nazivKviza)
//            pom.setNazivPredmeta(nazivPredmeta)
//            for(pom1 in listaSvihOdgovorenihPitanja){
//                if(pom1.equals(pom)){
//                    pom.setOdgovorNaPitanje(odgovor)
//                    break
//                }
//            }
//        }


    }

}